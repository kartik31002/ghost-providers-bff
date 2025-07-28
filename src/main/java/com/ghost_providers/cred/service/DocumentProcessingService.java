package com.ghost_providers.cred.service;

import com.ghost_providers.cred.dto.HealthInsuranceResponse;
import com.ghost_providers.cred.dto.MedicalLicenseResponse;
import com.ghost_providers.cred.dto.WorkExperienceResponse;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DocumentProcessingService {

    public void extractAndSave(File licensePdf, File insurancePdf, File workHistoryPdf, Long providerId) {
        PdfExtractorClient extractorClient = new PdfExtractorClient();

        WorkExperienceResponse workHistory = extractorClient.extract("work_experience", workHistoryPdf, WorkExperienceResponse.class);
        MedicalLicenseResponse license = extractorClient.extract("medical_license", licensePdf, MedicalLicenseResponse.class);
        HealthInsuranceResponse insurance = extractorClient.extract("health_insurance", insurancePdf, HealthInsuranceResponse.class);

    }

}
