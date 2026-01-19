package com.assess.docservice.config;

import com.assess.docservice.component.TenantRateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TenantRateLimitFilter> rateLimitFilter(
            TenantRateLimitFilter filter) {

        FilterRegistrationBean<TenantRateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setOrder(1); // before security if needed
        registration.addUrlPatterns("/*");
        return registration;
    }
}

