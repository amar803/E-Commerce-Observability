package com.amar.lab.order.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductClient {
    private static final Logger log = LoggerFactory.getLogger(ProductClient.class);

    private final WebClient webClient;
    private final String baseUrl;

    public ProductClient(WebClient webClient, @Value("${clients.product-service.base-url}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Retry(name = "productService")
    @CircuitBreaker(name = "productService", fallbackMethod = "productExistsFallback")
    public boolean productExists(Long productId) {
        Boolean exists = webClient.get()
                .uri(baseUrl + "/api/products/internal/{id}/exists", productId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class).map(msg ->
                        new RuntimeException("product-service error: " + msg)))
                .bodyToMono(Boolean.class)
                .block();
        return exists != null && exists;
    }

    @SuppressWarnings("unused")
    private boolean productExistsFallback(Long productId, Throwable t) {
        log.error("Fallback productExists. productId={} reason={}", productId, t.toString());
        throw new RuntimeException("Upstream product-service unavailable (circuit breaker)");
    }
}
