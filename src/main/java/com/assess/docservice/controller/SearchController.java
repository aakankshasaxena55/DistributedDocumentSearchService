package com.assess.docservice.controller;

import com.assess.docservice.model.SearchDocument;
import com.assess.docservice.service.SearchService;
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
    public List<SearchDocument> search(@RequestParam String q) {
        System.out.println("Search OK for query: " + q);
        return searchService.search(q);
    }

    @GetMapping("/documents")
    public List<SearchDocument> getDocuments() {

        return searchService.getDocumentsForCurrentTenant();
    }
}

