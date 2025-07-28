package com.ghost_providers.cred.service;

import com.ghost_providers.cred.dto.LicenseDto;
import com.ghost_providers.cred.dto.ProviderIntakeRequest;
import com.ghost_providers.cred.dto.SpecialtyDto;
import com.ghost_providers.cred.model.*;
import com.ghost_providers.cred.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProviderIntakeService {

    private final ProviderRepository providerRepository;
    private final ProviderCredentialRepository credentialRepository;
    private final ProviderLicenseRepository licenseRepository;
    private final ProviderSpecialtyRepository specialtyRepository;
    private final ProviderContactRepository contactRepository;
    private final DemographicsRepository demographicsRepository;
    private final ProviderPsvStatusRepository psvStatusRepository;

    @Transactional
    public Long createProvider(ProviderIntakeRequest request) {
        // 1. Save Provider
        Provider provider = new Provider();
        provider.setFirstName(request.getFirstName());
        provider.setLastName(request.getLastName());
        provider.setEmail(request.getEmail());
        provider.setPhone(request.getPhone());

        provider.setStatus(Provider.Status.NEW); // Default
        provider.setCreatedAt(LocalDateTime.now()); // Default
        provider.setUpdatedAt(LocalDateTime.now()); // Default
        provider.setIntakeSource(Provider.IntakeSource.valueOf(request.getIntakeSource())); //Default
        Provider saved = providerRepository.save(provider);

        if(request.getDemographics()!=null) {
            Demographics demographics = new Demographics();
            demographics.setProvider(saved);
            demographics.setSsn(request.getDemographics().getSsn());
            demographics.setDateOfBirth(request.getDemographics().getDateOfBirth());
            demographics.setGender(Demographics.Gender.valueOf(request.getDemographics().getGender().toUpperCase()));
            demographicsRepository.save(demographics);
        }

        // 2. Save Credentials
        if(request.getCredentials()!=null) {
            ProviderCredential cred = new ProviderCredential();
            cred.setProvider(saved);
            cred.setNpi(request.getCredentials().getNpi());
            cred.setTin(request.getCredentials().getTin());
            cred.setDeaNumber(request.getCredentials().getDeaNumber());
            credentialRepository.save(cred);
        }

        if(request.getContactInfo()!=null) {
            ProviderContact contact = getContact(request, saved);
            contactRepository.save(contact);
        }

        // 3. Save Licenses
        for (LicenseDto licenseDto : request.getLicenses()) {
            ProviderLicense license = new ProviderLicense();
            license.setProvider(saved);
            license.setState(licenseDto.getState());
            license.setLicenseNumber(licenseDto.getLicenseNumber());
            license.setExpirationDate(licenseDto.getExpirationDate());
            license.setStatus(ProviderLicense.LicenseStatus.valueOf(licenseDto.getStatus().toUpperCase()));
            licenseRepository.save(license);
        }

        // 4. Save Specialties
        for (SpecialtyDto s : request.getSpecialties()) {
            ProviderSpecialty sp = new ProviderSpecialty();
            sp.setProvider(saved);
            sp.setTaxonomyCode(s.getTaxonomyCode());
            sp.setDescription(s.getDescription());
            sp.setPrimary(s.isPrimary());
            specialtyRepository.save(sp);
        }

        ProviderPsvStatus psvStatus = getPsvStatus(saved);
        psvStatusRepository.save(psvStatus);

        return saved.getId();
    }

    public static ProviderPsvStatus getPsvStatus(Provider saved) {
        ProviderPsvStatus psvStatus = new ProviderPsvStatus();
        psvStatus.setProvider(saved);
        return psvStatus;
    }

    public static ProviderContact getContact(ProviderIntakeRequest request, Provider saved) {
        ProviderContact contact = new ProviderContact();
        contact.setProvider(saved);
        contact.setEmail(request.getContactInfo().getEmail());
        contact.setPhone(request.getContactInfo().getPhone());
        contact.setAddress(request.getContactInfo().getAddress());
        contact.setCity(request.getContactInfo().getCity());
        contact.setState(request.getContactInfo().getState());
        contact.setZip(request.getContactInfo().getZip());
        contact.setCountry(request.getContactInfo().getCountry());
        return contact;
    }
}
