package com.ghost_providers.cred.controller;

import com.ghost_providers.cred.dto.ProviderIntakeRequest;
import com.ghost_providers.cred.model.FileDocument;
import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderDocument;
import com.ghost_providers.cred.repository.FileDocumentRepository;
import com.ghost_providers.cred.repository.ProviderDocumentRepository;
import com.ghost_providers.cred.repository.ProviderRepository;
import com.ghost_providers.cred.service.DocumentProcessingService;
import com.ghost_providers.cred.service.FileProcessingService;
import com.ghost_providers.cred.service.ProviderIntakeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {
    private final FileDocumentRepository repository;

    private final FileProcessingService fileProcessingService;

    private final ProviderIntakeService providerIntakeService;
    private final ProviderRepository providerRepository;
    private final ProviderDocumentRepository providerDocumentRepository;
    private final DocumentProcessingService documentProcessingService;

    private final String uploadDir = System.getProperty("user.home") + "/uploaded-files";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, uniqueName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            FileDocument doc = new FileDocument();
            doc.setFileName(file.getOriginalFilename());
            doc.setFileSize(file.getSize());
            doc.setFileType(file.getContentType());
            doc.setFilePath(path.toString());
            doc.setStatus(FileDocument.FileStatus.NEW);
            doc.setProvidersFound(0);
            repository.save(doc);

            return ResponseEntity.ok(Map.of("message", "File uploaded", "id", doc.getId()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Upload failed"));
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadProviderDocument(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, uniqueName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Optional<Provider> optionalProvider = providerRepository.findById(id);
            if(optionalProvider.isEmpty()){
                return ResponseEntity.ok(Map.of("message", "Provider does not exist", "id", id));
            }
            String type = file.getOriginalFilename().endsWith(".pdf") ?
                    "PDF" : file.getOriginalFilename().endsWith(".zip") ?
                    "ZIP" : null;

            if(type == null) {
                return ResponseEntity.ok(Map.of("message", "Invalid file type", "id", id));
            }

            ProviderDocument doc = new ProviderDocument();
            doc.setProvider(optionalProvider.get());
            doc.setType(type);
            doc.setFilename(uniqueName);
            providerDocumentRepository.save(doc);

            return ResponseEntity.ok(Map.of("message", "File uploaded", "id", doc.getId()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Upload failed"));
        }
    }

    @GetMapping
    public ResponseEntity<List<FileDocument>> listAllFiles() {
        return ResponseEntity.ok(repository.findAll(Sort.by(Sort.Direction.DESC, "uploadTime")));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        return repository.findById(id).map(doc -> {
            Path path = Paths.get(doc.getFilePath());
            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }
            try {
                byte[] data = Files.readAllBytes(path);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(doc.getFileType()));
                headers.setContentDisposition(ContentDisposition.inline().filename(doc.getFileName()).build());
                return new ResponseEntity<>(data, headers, HttpStatus.OK);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error reading file"));
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload/providers")
    public ResponseEntity<?> uploadProviderZip(@RequestParam("file") MultipartFile zipFile) {
        String baseDir = "./temp";
        String uniqueFolder = "unzipped_" + System.currentTimeMillis();
        File extractDir = new File(baseDir, uniqueFolder);
        try {
            if (!zipFile.getOriginalFilename().endsWith(".zip")) {
                return ResponseEntity.badRequest().body("Only ZIP files are supported.");
            }

            // 1. Save the ZIP file
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) uploadDirectory.mkdirs();

            String uniqueZipName = UUID.randomUUID() + "_" + zipFile.getOriginalFilename();
            Path zipPath = Paths.get(uploadDir, uniqueZipName);
            Files.copy(zipFile.getInputStream(), zipPath, StandardCopyOption.REPLACE_EXISTING);


            // Step 1: Create custom temp dir
            if (!extractDir.mkdirs()) {
                return ResponseEntity.status(500).body("Failed to create temp directory.");
            }

            try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.isDirectory()) continue; // ✅ Skip folders

                    File newFile = new File(extractDir, entry.getName());

                    // Prevent Zip Slip
                    String destDirPath = extractDir.getCanonicalPath();
                    String destFilePath = newFile.getCanonicalPath();
                    if (!destFilePath.startsWith(destDirPath + File.separator)) {
                        throw new IOException("Entry outside target dir: " + entry.getName());
                    }

                    // ✅ Ensure parent directory exists
                    File parent = newFile.getParentFile();
                    if (!parent.exists() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory: " + parent);
                    }

                    // ✅ Only try to write if it's not a directory
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        zis.transferTo(fos);
                    }
                }
            }

            File[] extractedFiles = extractDir.listFiles();
            if (extractedFiles == null || extractedFiles.length < 4)
                return ResponseEntity.badRequest().body("ZIP must contain at least 1 CSV and 3 PDFs.");

            File csvFile = Arrays.stream(extractedFiles)
                    .filter(f -> f.getName().endsWith(".csv"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("CSV file not found in ZIP"));

            File licensePdf = findFileByKeyword(extractedFiles, "license");
            File insurancePdf = findFileByKeyword(extractedFiles, "insurance");
            File workHistoryPdf = findFileByKeyword(extractedFiles, "work");

            // 4. Parse provider data from CSV
            List<ProviderIntakeRequest> providers = fileProcessingService.parseCsv(new FileInputStream(csvFile));

            // 5. Save each provider
            Long providerId = null;
            for (ProviderIntakeRequest provider : providers) {
                providerId = providerIntakeService.createProvider(provider);
            }

            // 6. Save FileDocument entry
            FileDocument doc = new FileDocument();
            doc.setFileName(zipFile.getOriginalFilename());
            doc.setFileSize(Files.size(zipPath));
            doc.setFileType(zipFile.getContentType());
            doc.setFilePath(zipPath.toString());
            doc.setStatus(FileDocument.FileStatus.NEW);
            doc.setProvidersFound(providers.size());
            repository.save(doc);

            // 7. OPTIONAL: Send PDF content to external API
            documentProcessingService.extractAndSave(licensePdf, insurancePdf, workHistoryPdf, providerId);

            return ResponseEntity.ok(Map.of(
                    "message", "Processed " + providers.size() + " providers from ZIP",
                    "fileId", doc.getId()
            ));

        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(Map.of("error", "ZIP processing failed", "details", e.getMessage()));
        } finally {
            try {
                deleteDirectoryRecursively(extractDir); // optional cleanup
            } catch (IOException cleanupEx) {
                System.err.println("Cleanup failed: " + cleanupEx.getMessage());
            }
        }
    }

    private static File[] getExtractedFiles(MultipartFile zipFile, Path extractPath) throws IOException {
        File extractDir = extractPath.toFile();

        // Safely extract with directory check and mkdirs
        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(extractDir, entry.getName());

                // Prevent zip slip
                String destDirPath = extractDir.getCanonicalPath();
                String destFilePath = newFile.getCanonicalPath();
                if (!destFilePath.startsWith(destDirPath + File.separator)) {
                    throw new IOException("Zip entry outside target directory: " + entry.getName());
                }

                // Ensure parent directory exists
                File parentDir = newFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    zis.transferTo(fos);
                }
            }
        }

        // 3. Identify the files inside ZIP
        File[] extractedFiles = extractDir.listFiles();
        return extractedFiles;
    }

    private File findFileByKeyword(File[] files, String keyword) {
        return Arrays.stream(files)
                .filter(f -> f.getName().toLowerCase().contains(keyword.toLowerCase()) && f.getName().endsWith(".pdf"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing PDF file with keyword: " + keyword));
    }

    private void deleteDirectoryRecursively(File dir) throws IOException {
        if (dir.isDirectory()) {
            File[] entries = dir.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectoryRecursively(entry);
                }
            }
        }
        if (!dir.delete()) {
            throw new IOException("Failed to delete " + dir.getAbsolutePath());
        }
    }

}
