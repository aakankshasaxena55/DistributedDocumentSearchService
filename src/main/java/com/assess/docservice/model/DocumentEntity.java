package com.assess.docservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "documents")
@Data
public class DocumentEntity {

    @Id
    private String id;

    private String tenantId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    public DocumentEntity(String id, String tenantId, String title, String content) {
    }
}

