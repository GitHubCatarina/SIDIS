package com.example.serviceLendingQuery.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RetryConfig {
    // Apenas habilita o uso de @Retryable em toda a aplicação
}
