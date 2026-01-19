package com.assess.docservice.service;

import com.assess.docservice.model.DocumentEntity;
import com.assess.docservice.model.SearchDocument;
import com.assess.docservice.repository.SearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.assess.docservice.repository.DocumentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository repository;
    private final SearchRepository searchRepository; // ES

    public DocumentService(DocumentRepository repository, SearchRepository searchRepository) {
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @CacheEvict(value = "searchCache", allEntries = true)
    public DocumentEntity save(DocumentEntity doc, String tenantId) {
        doc.setTenantId(tenantId); // enforce tenant

        // save in database
        DocumentEntity saved = repository.save(doc);

        // index in Elasticsearch
        SearchDocument searchDoc = new SearchDocument();
        searchDoc.setId(saved.getId());
        searchDoc.setTenantId(tenantId);
        searchDoc.setTitle(saved.getTitle());
        searchDoc.setContent(saved.getContent());
        searchDoc.setTags(saved.getTags());
        searchDoc.setCreatedAt(saved.getCreatedAt() != null ? saved.getCreatedAt() : Instant.now());

        searchRepository.save(searchDoc);

        log.info("📄 Document saved & indexed | id={} | tenant={}", saved.getId(), tenantId);

        return saved;
    }

    public List<DocumentEntity> findByTenant(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public void delete(Long id, String tenantId) {
        // Optional: check if document belongs to tenant
        repository.findById(id).ifPresent(doc -> {
            if (doc.getTenantId().equals(tenantId)) {
                repository.delete(doc);
                searchRepository.deleteById(id); // remove from ES
                log.info("🗑️ Document deleted | id={} | tenant={}", id, tenantId);
            } else {
                throw new SecurityException("Unauthorized: document belongs to another tenant");
            }
        });
    }

    public Optional<DocumentEntity> findById(Long id, String tenantId) {
        return repository.findById(id)
                .filter(doc -> doc.getTenantId().equals(tenantId));
    }


}
