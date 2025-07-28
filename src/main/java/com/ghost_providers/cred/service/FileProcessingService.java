package com.ghost_providers.cred.service;

import com.ghost_providers.cred.dto.*;
import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderLicense;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Slf4j
public class FileProcessingService {
    public List<ProviderIntakeRequest> parseCsv(InputStream in) throws IOException {
        List<ProviderIntakeRequest> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(in))) {
            String[] headers = reader.readNext();
            Map<String, Integer> indexMap = mapHeaders(headers);

            String[] line;
            while ((line = reader.readNext()) != null) {
                ProviderIntakeRequest dto = extractProviderFromRow(line, indexMap);
                result.add(dto);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<ProviderIntakeRequest> parseExcel(InputStream in) throws IOException {
        List<ProviderIntakeRequest> result = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(in);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        Map<String, Integer> indexMap = new HashMap<>();
        if (rows.hasNext()) {
            Row header = rows.next();
            for (Cell cell : header) {
                indexMap.put(cell.getStringCellValue().trim().toLowerCase(), cell.getColumnIndex());
            }
        }

        while (rows.hasNext()) {
            Row row = rows.next();
            ProviderIntakeRequest dto = new ProviderIntakeRequest();
            dto.setFirstName(getStringCell(row, indexMap.get("first name")));
            dto.setLastName(getStringCell(row, indexMap.get("last name")));
            dto.setEmail(getStringCell(row, indexMap.get("email address")));
            dto.setPhone(getStringCell(row, indexMap.get("phone number")));
            dto.setIntakeSource(Provider.IntakeSource.FILE_UPLOAD.getLabel());
//            dto.setSpecialty(getStringCell(row, indexMap.get("specialty")));
//            dto.setNpi(getStringCell(row, indexMap.get("npi number")));
//            dto.setTin(getStringCell(row, indexMap.get("tin")));
//            dto.setLicenseNumber(getStringCell(row, indexMap.get("license number")));
//            dto.setDeaNumber(getStringCell(row, indexMap.get("dea number")));
            CredentialsDto credentialsDto = new CredentialsDto();
            credentialsDto.setNpi(getStringCell(row, indexMap.get("npi")));
            credentialsDto.setTin(getStringCell(row, indexMap.get("tin")));
            credentialsDto.setDeaNumber(getStringCell(row, indexMap.get("dea number")));
            dto.setCredentials(credentialsDto);

            LicenseDto licenseDto = new LicenseDto();
            licenseDto.setLicenseNumber(getStringCell(row, indexMap.get("license number")));
            licenseDto.setStatus(ProviderLicense.LicenseStatus.PENDING.getLabel());
            dto.setLicenses(List.of(licenseDto));

            SpecialtyDto specialtyDto = new SpecialtyDto();
            specialtyDto.setDescription(getStringCell(row, indexMap.get("specialty")));

            dto.setSpecialties(List.of(specialtyDto));

            dto.setIntakeSource(Provider.IntakeSource.FILE_UPLOAD.getLabel());

            result.add(dto);
        }
        return result;
    }

    private String getStringCell(Row row, Integer index) {
        if (index == null || row.getCell(index) == null) return null;
        Cell cell = row.getCell(index);
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }


    private Map<String, Integer> mapHeaders(String[] headers) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            indexMap.put(headers[i].trim().toLowerCase(), i);
        }
        return indexMap;
    }

    private ProviderIntakeRequest extractProviderFromRow(String[] row, Map<String, Integer> map) {
        try {
            ProviderIntakeRequest dto = new ProviderIntakeRequest();
            dto.setFirstName(row[map.getOrDefault("first name", -1)]);
            dto.setLastName(row[map.getOrDefault("last name", -1)]);
            dto.setEmail(row[map.getOrDefault("email address", -1)]);
            dto.setPhone(row[map.getOrDefault("phone number", -1)]);
//        dto.setSpecialty(row[map.getOrDefault("specialty", -1)]);
//        dto.setNpi(row[map.getOrDefault("npi number", -1)]);
//        dto.setTin(row[map.getOrDefault("tin", -1)]);
//        dto.setLicenseNumber(row[map.getOrDefault("license number", -1)]);
//        dto.setDeaNumber(row[map.getOrDefault("dea number", -1)]);

            CredentialsDto credentialsDto = new CredentialsDto();
            credentialsDto.setNpi(row[map.getOrDefault("npi number", null)]);
            credentialsDto.setTin(row[map.getOrDefault("tin", null)]);
            credentialsDto.setDeaNumber(row[map.getOrDefault("dea number", null)]);
            dto.setCredentials(credentialsDto);

            LicenseDto licenseDto = new LicenseDto();
            licenseDto.setLicenseNumber(row[map.getOrDefault("license number", null)]);
            licenseDto.setStatus(ProviderLicense.LicenseStatus.PENDING.getLabel());
            dto.setLicenses(List.of(licenseDto));

            SpecialtyDto specialtyDto = new SpecialtyDto();
            specialtyDto.setDescription(row[map.getOrDefault("specialty", null)]);

            dto.setSpecialties(List.of(specialtyDto));

            dto.setIntakeSource(Provider.IntakeSource.FILE_UPLOAD.getLabel());

            return dto;
        } catch (Exception e) {
            log.error("Error while extractProviderFromRow", e);
            return null;
        }
    }


}
