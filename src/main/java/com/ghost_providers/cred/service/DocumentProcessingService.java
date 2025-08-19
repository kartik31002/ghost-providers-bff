package com.ghost_providers.cred.service;

import com.ghost_providers.cred.dto.*;
import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderLicense;
import com.ghost_providers.cred.model.ProviderWorkHistory;
import com.ghost_providers.cred.repository.ProviderCredentialRepository;
import com.ghost_providers.cred.repository.ProviderLicenseRepository;
import com.ghost_providers.cred.repository.ProviderRepository;
import com.ghost_providers.cred.repository.ProviderWorkHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DocumentProcessingService {

    private final ProviderRepository providerRepository;
    private final ProviderLicenseRepository providerLicenseRepository;
    private final ProviderWorkHistoryRepository providerWorkHistoryRepository;
    private final ProviderCredentialRepository providerCredentialRepository;

    @Async
    public void extractAndSave(File licensePdf, File insurancePdf, File workHistoryPdf, Long providerId) {
        PdfExtractorClient extractorClient = new PdfExtractorClient();

// Work Experience
        WorkExperienceResponse workHistory = extractorClient.extract(
                "work_experience",
                workHistoryPdf,
                new ParameterizedTypeReference<PdfExtractionWrapper<WorkExperienceResponse>>() {}
        ).getExtractedInfo();

// Medical License
        MedicalLicenseResponse license = extractorClient.extract(
                "medical_license",
                licensePdf,
                new ParameterizedTypeReference<PdfExtractionWrapper<MedicalLicenseResponse>>() {}
        ).getExtractedInfo();

// Health Insurance
        HealthInsuranceResponse insurance = extractorClient.extract(
                "health_insurance",
                insurancePdf,
                new ParameterizedTypeReference<PdfExtractionWrapper<HealthInsuranceResponse>>() {}
        ).getExtractedInfo();

        Provider provider = providerRepository.findById(providerId).orElseThrow(() -> new RuntimeException("Provider not found"));

        if (license != null) {
            ProviderLicense providerLicense = new ProviderLicense();
            providerLicense.setProvider(provider);
            providerLicense.setLicenseNumber(license.getLicenseNumber());
            providerLicense.setState(license.getIssuingState());
            providerLicense.setExpirationDate(license.getLicenseExpirationDate()==null ? null : LocalDate.parse(license.getLicenseExpirationDate()));
            providerLicenseRepository.save(providerLicense);
        }

        if (workHistory != null && workHistory.getEmploymentHistory() != null) {
            for (com.ghost_providers.cred.dto.EmploymentHistoryEntry entry : workHistory.getEmploymentHistory()) {
                ProviderWorkHistory providerWorkHistory = getProviderWorkHistory(entry, provider);
                providerWorkHistoryRepository.save(providerWorkHistory);
            }
        }
        if (insurance != null && provider.getCredentials()!=null) {
            provider.getCredentials().setInsurancePolicyNumber(insurance.getPolicyNumber());
            providerCredentialRepository.save(provider.getCredentials());
        }
    }

    private static ProviderWorkHistory getProviderWorkHistory(EmploymentHistoryEntry entry, Provider provider) {
        ProviderWorkHistory providerWorkHistory = new ProviderWorkHistory();
        providerWorkHistory.setProvider(provider);
        providerWorkHistory.setOrganization(entry.getOrganization());
        providerWorkHistory.setRole(entry.getRole());
        providerWorkHistory.setStartDate(entry.getStartDate() == null ? null : LocalDate.parse(entry.getStartDate()));
        providerWorkHistory.setEndDate(entry.getEndDate() == null ? null : LocalDate.parse(entry.getEndDate()));
        providerWorkHistory.setGapFlag(entry.isGapFlag());
        return providerWorkHistory;
    }

}
