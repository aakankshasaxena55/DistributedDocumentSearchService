package com.assess.docservice.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "documents")
@Data
public class SearchDocument {

    @Id
    private String id;

    private String tenantId;
    private String title;
    private String content;
}

