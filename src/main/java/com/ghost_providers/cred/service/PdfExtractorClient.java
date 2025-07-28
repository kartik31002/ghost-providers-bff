package com.ghost_providers.cred.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class PdfExtractorClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> T extract(String documentType, File pdf, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(pdf));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        String url = "http://localhost:8130/extract-pdf/" + documentType;

        return restTemplate.postForObject(url, request, responseClass);
    }
}
