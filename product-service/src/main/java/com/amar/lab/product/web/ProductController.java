package com.amar.lab.product.web;

import com.amar.lab.common.api.ApiResponse;
import com.amar.lab.common.web.TraceIdFilter;
import com.amar.lab.product.service.ProductService;
import com.amar.lab.product.web.dto.ProductRequest;
import com.amar.lab.product.web.dto.ProductResponse;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> list() {
        return ApiResponse.ok("ok", service.list(), MDC.get(TraceIdFilter.TRACE_ID));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> get(@PathVariable Long id) {
        return ApiResponse.ok("ok", service.get(id), MDC.get(TraceIdFilter.TRACE_ID));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        return ApiResponse.ok("created", service.create(req), MDC.get(TraceIdFilter.TRACE_ID));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        return ApiResponse.ok("updated", service.update(id, req), MDC.get(TraceIdFilter.TRACE_ID));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/internal/{id}/exists")
    public boolean exists(@PathVariable Long id) {
        return service.exists(id);
    }
}
