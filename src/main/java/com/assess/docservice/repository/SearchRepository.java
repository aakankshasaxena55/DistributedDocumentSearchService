package com.assess.docservice.repository;

import com.assess.docservice.model.SearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchRepository extends ElasticsearchRepository<SearchDocument, String> {

    List<SearchDocument> findByTenantId(String tenantId);
}
