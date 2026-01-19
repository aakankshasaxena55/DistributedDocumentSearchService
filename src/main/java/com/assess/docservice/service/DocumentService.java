package com.assess.docservice.service;

import com.assess.docservice.model.DocumentEntity;
import com.assess.docservice.model.SearchDocument;
import com.assess.docservice.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public DocumentEntity save(DocumentEntity doc, String tenantId) {
        doc.setTenantId(tenantId); // enforce tenant
        return repository.save(doc);
    }

    public List<DocumentEntity> findByTenant(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public void delete(Long id, String tenantId) {
        // Optional: check if document belongs to tenant
        repository.findById(id).ifPresent(doc -> {
            if (doc.getTenantId().equals(tenantId)) {
                repository.delete(doc);
            } else {
                throw new SecurityException("Unauthorized: document belongs to another tenant");
            }
        });
    }

    public DocumentEntity findById(Long id, String tenantId) {
        return repository.findById(id)
                .filter(doc -> doc.getTenantId().equals(tenantId))
                .orElseThrow(() -> new SecurityException("Unauthorized or Not Found"));
    }

}
