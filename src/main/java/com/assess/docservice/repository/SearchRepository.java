package com.assess.docservice.repository;

import com.assess.docservice.model.SearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchRepository extends ElasticsearchRepository<SearchDocument, Long> {

    List<SearchDocument> findByTenantId(String tenantId);

    // search by tenant and query in title/content
    List<SearchDocument> findByTenantIdAndTitleContainingIgnoreCaseOrTenantIdAndContentContainingIgnoreCase(
            String tenantId1, String title,
            String tenantId2, String content
    );
}
