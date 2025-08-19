package com.ghost_providers.cred.service;

import com.ghost_providers.cred.dto.PdfExtractionWrapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Component
public class PdfExtractorClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> PdfExtractionWrapper<T> extract(String documentType, File pdf, ParameterizedTypeReference<PdfExtractionWrapper<T>> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(pdf));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<PdfExtractionWrapper<T>> response = restTemplate.exchange(
                "http://localhost:8130/extract-pdf/" + documentType,
                HttpMethod.POST,
                request,
                responseType
        );
        System.out.println(response.getBody());

        return response.getBody();
    }
}
