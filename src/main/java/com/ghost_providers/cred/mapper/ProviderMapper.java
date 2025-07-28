package com.ghost_providers.cred.mapper;

import com.ghost_providers.cred.dto.*;
import com.ghost_providers.cred.model.Provider;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ProviderMapper {

    public static ProviderDTO toDTO(Provider provider) {
        ProviderDTO dto = new ProviderDTO();

        dto.setId(provider.getId());
        dto.setName("Dr. "+provider.getFirstName() + " "+provider.getLastName());
        dto.setFirstName(provider.getFirstName());
        dto.setLastName(provider.getLastName());
        dto.setRoId(provider.getRoId());
        dto.setEmail(provider.getEmail());
        dto.setPhone(provider.getPhone());
        dto.setIntakeSource(provider.getIntakeSource().getLabel());
        dto.setStatus(provider.getStatus().getLabel());
        dto.setCreatedAt(provider.getCreatedAt());
        dto.setUpdatedAt(provider.getUpdatedAt());

        // Demographics
        if(provider.getDemographics()!=null) {
            DemographicsDto demo = new DemographicsDto();
            demo.setDateOfBirth(provider.getDemographics().getDateOfBirth());
            demo.setGender(provider.getDemographics().getGender().name());
            demo.setSsn(provider.getDemographics().getSsn());
            dto.setDemographics(demo);
        }

        // Contact
        if(provider.getContact()!=null) {
            ContactDto contact = new ContactDto();
            contact.setAddress(provider.getContact().getAddress());
            contact.setCity(provider.getContact().getCity());
            contact.setState(provider.getContact().getState());
            contact.setZip(provider.getContact().getZip());
            contact.setCountry(provider.getContact().getCountry());
            contact.setPhone(provider.getContact().getPhone());
            contact.setEmail(provider.getContact().getEmail());
            dto.setContact(contact);
        }

        // Credentials
        if(provider.getCredentials()!=null) {
            CredentialsDto cred = new CredentialsDto();
            cred.setNpi(provider.getCredentials().getNpi());
            cred.setTin(provider.getCredentials().getTin());
            cred.setDeaNumber(provider.getCredentials().getDeaNumber());


            cred.setStateLicenses(
                    provider.getStateLicenses().stream().map(license -> {
                        LicenseDto l = new LicenseDto();
                        l.setState(license.getState());
                        l.setLicenseNumber(license.getLicenseNumber());
                        l.setExpirationDate(license.getExpirationDate());
                        l.setStatus(license.getStatus().name());
                        return l;
                    }).collect(Collectors.toList())
            );


            cred.setSpecialties(
                    provider.getSpecialties().stream().map(s -> {
                        SpecialtyDto sd = new SpecialtyDto();
                        sd.setTaxonomyCode(s.getTaxonomyCode());
                        sd.setDescription(s.getDescription());
                        sd.setPrimary(s.isPrimary());
                        return sd;
                    }).collect(Collectors.toList())
            );

            dto.setCredentials(cred);
        }

        // PSV Status
        PsvStatusDto psv = new PsvStatusDto();
        psv.setLicenses(provider.getPsvStatus().getLicenses().name());
        psv.setDeaNpi(provider.getPsvStatus().getDeaNpi().name());
        psv.setEducation(provider.getPsvStatus().getEducation().name());
        psv.setMalpractice(provider.getPsvStatus().getMalpractice().name());
        psv.setWorkHistory(provider.getPsvStatus().getWorkHistory().name());
        psv.setSanctions(provider.getPsvStatus().getSanctions().name());
        psv.setOverallStatus(provider.getPsvStatus().getOverallStatus().name());
        dto.setPsvStatus(psv);

        // Validation Errors
        dto.setValidationErrors(
                provider.getValidationErrors().stream().map(err -> {
                    ValidationErrorDto ve = new ValidationErrorDto();
                    ve.setField(err.getField());
                    ve.setMessage(err.getMessage());
                    ve.setSeverity(err.getSeverity().name());
                    return ve;
                }).collect(Collectors.toList())
        );

        return dto;
    }
}

