package com.assess.docservice.controller;

import com.assess.docservice.service.TenantRateLimiter;
import com.assess.docservice.model.DocumentEntity;
import com.assess.docservice.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.assess.docservice.component.TenantContext;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;
    private final TenantRateLimiter rateLimiterService;

    public DocumentController(DocumentService documentService,
                              TenantRateLimiter rateLimiterService) {
        this.documentService = documentService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping
    public ResponseEntity<DocumentEntity> addDocument(@RequestBody DocumentEntity doc) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(documentService.save(doc, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> getDocument(@PathVariable Long id) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(documentService.findById(id, tenantId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<?> getDocuments() {

        log.info("Inside Document Controller");

        String tenantId = TenantContext.getTenantId();

        if (!rateLimiterService.tryConsume(tenantId)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeded");
        }

        return ResponseEntity.ok(documentService.findByTenant(tenantId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        String tenantId = TenantContext.getTenantId();
        documentService.delete(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
