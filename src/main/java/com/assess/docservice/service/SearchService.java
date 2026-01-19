package com.assess.docservice.service;

import com.assess.docservice.model.DocumentEntity;
import com.assess.docservice.model.SearchDocument;
import com.assess.docservice.repository.DocumentRepository;
import com.assess.docservice.repository.SearchRepository;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final SearchRepository repository;

    public SearchService(ElasticsearchOperations elasticsearchOperations, SearchRepository repository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.repository = repository;
    }

    @Cacheable(value = "search", key = "#p0 + ':' + #p1.toLowerCase()")
    public List<SearchDocument> search(String tenantId, String searchText) {

        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .match(t -> t
                                                .field("content")
                                                .query(searchText)
                                        )
                                )
                                .filter(f -> f
                                        .term(t -> t
                                                .field("tenantId")
                                                .value(tenantId)
                                        )
                                )
                        )
                )
                .build();

        return elasticsearchOperations
                .search(query, SearchDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public List<SearchDocument> search(String q) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenantId = (String) auth.getDetails();

        return repository.findByTenantId(tenantId);
    }

    public List<SearchDocument> getDocumentsForCurrentTenant() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenantId = (String) auth.getDetails();
        return repository.findByTenantId(tenantId);
    }

}

