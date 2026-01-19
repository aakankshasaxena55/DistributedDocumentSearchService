package com.assess.docservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
        basePackages = "com.assess.docservice.repository"
)
public class ElasticsearchConfig {
    // Spring Boot auto-configures Elasticsearch client
}
