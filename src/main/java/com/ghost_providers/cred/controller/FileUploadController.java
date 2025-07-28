package com.ghost_providers.cred.controller;

import com.ghost_providers.cred.dto.ProviderDTO;
import com.ghost_providers.cred.dto.ProviderIntakeRequest;
import com.ghost_providers.cred.model.FileDocument;
import com.ghost_providers.cred.repository.FileDocumentRepository;
import com.ghost_providers.cred.service.FileProcessingService;
import com.ghost_providers.cred.service.ProviderIntakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileDocumentRepository repository;

    private final FileProcessingService fileProcessingService;

    private final ProviderIntakeService providerIntakeService;

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
    public ResponseEntity<?> uploadProviderFile(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Ensure upload directory exists
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            // 2. Save file to disk
            String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, uniqueName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 3. Save FileDocument entry
            FileDocument doc = new FileDocument();
            doc.setFileName(file.getOriginalFilename());
            doc.setFileSize(Files.size(path)); // more reliable
            doc.setFileType(file.getContentType());
            doc.setFilePath(path.toString());
            doc.setStatus(FileDocument.FileStatus.NEW);
            doc.setProvidersFound(0);
            repository.save(doc);

            String filename = file.getOriginalFilename();
            List<ProviderIntakeRequest> providers;

            if (filename.endsWith(".csv")) {
                providers = fileProcessingService.parseCsv(file.getInputStream());
            } else if (filename.endsWith(".xls") || filename.endsWith(".xlsx")) {
                providers = fileProcessingService.parseExcel(file.getInputStream());
            } else {
                return ResponseEntity.badRequest().body("Unsupported file type.");
            }
            // Save each provider
            providers.forEach(providerIntakeService::createProvider);
            doc.setProvidersFound(providers.size());
            doc.setStatus(FileDocument.FileStatus.NEW);
            repository.save(doc);

            return ResponseEntity.ok(Map.of("message", "Imported " + providers.size() + " providers"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to parse file", "details", e.getMessage()));
        }
    }

}
