package com.assess.docservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.assess.docservice.model.SearchDocument;
import com.assess.docservice.repository.SearchRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
public class SearchService {

    private static final Logger log =
            LoggerFactory.getLogger(SearchService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    private final SearchRepository searchRepository;

    public SearchService(ElasticsearchOperations elasticsearchOperations, SearchRepository searchRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.searchRepository = searchRepository;
    }

    @Cacheable(value = "searchCache", key = "#tenantId + '-' + #query")
    public List<SearchDocument> search(String tenantId, String query) {
        log.info("🔍 Searching for '{}' in tenant '{}'", query, tenantId);

        return searchRepository.findByTenantIdAndTitleContainingIgnoreCaseOrTenantIdAndContentContainingIgnoreCase(
                tenantId, query,
                tenantId, query
        );
    }

    public List<SearchDocument> getDocumentsForTenant(String tenantId) {

        log.info("📄 Fetching all documents for tenant '{}'", tenantId);

        return searchRepository.findByTenantId(tenantId);

    }

}

