package com.assess.docservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "documents")
@Data
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "document_tags",
            joinColumns = @JoinColumn(name = "document_id")
    )
    @Column(name = "tag")
    private List<String> tags;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /* ---------- Lifecycle ---------- */

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    /* ---------- Constructors ---------- */

    public DocumentEntity() {
        // Required by JPA
    }

    public DocumentEntity(String tenantId, String title, String content, List<String> tags) {
        this.tenantId = tenantId;
        this.title = title;
        this.content = content;
        this.tags = tags;
    }
}
