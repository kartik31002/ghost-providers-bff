package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;
    private Long fileSize;

    private String filePath;

    private LocalDateTime uploadTime;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    private int providersFound;

    @PrePersist
    protected void onCreate() {
        this.uploadTime = LocalDateTime.now();
        this.status = FileStatus.NEW;
    }

    public enum FileStatus {
        NEW,
        PROCESSING,
        COMPLETED,
        VALIDATED,
        FAILED
    }
}
