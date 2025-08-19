package com.ghost_providers.cred.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PdfExtractionWrapper<T> {

    @JsonProperty("document_type")
    private String documentType;

    @JsonProperty("extracted_info")
    private T extractedInfo;

    @JsonProperty("raw_document_content_sample")
    private String rawDocumentContentSample;
}

