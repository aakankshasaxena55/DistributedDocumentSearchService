package com.assess.docservice.controller;

import com.assess.docservice.component.TenantContext;
import com.assess.docservice.model.SearchDocument;
import com.assess.docservice.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.SearchResult;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<SearchDocument>> search(
            @RequestParam String q,
            @RequestParam String tenant) {

        String jwtTenant = TenantContext.getTenantId();

        // ✅ normalize input
        tenant = tenant.trim();

        // 🔐 Prevent cross-tenant access
        if (!tenant.equals(jwtTenant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<SearchDocument> results =
                searchService.search(tenant, q);

        return results.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(results);
    }

    @GetMapping("/documents")
    public List<SearchDocument> getDocuments(@RequestParam String tenant) {
        return searchService.getDocumentsForTenant(tenant);
    }
}

