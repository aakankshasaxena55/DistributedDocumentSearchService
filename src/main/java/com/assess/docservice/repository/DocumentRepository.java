package com.assess.docservice.repository;

import com.assess.docservice.model.DocumentEntity;
import com.assess.docservice.model.SearchDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    List<DocumentEntity> findByTenantId(String tenantId);

}
