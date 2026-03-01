package com.amar.lab.product.service;

import com.amar.lab.product.domain.Product;
import com.amar.lab.product.repo.ProductRepository;
import com.amar.lab.product.web.dto.ProductRequest;
import com.amar.lab.product.web.dto.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        Product p = repo.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
        return toResponse(p);
    }

    @Transactional
    public ProductResponse create(ProductRequest req) {
        try {
            Product p = new Product(req.name(), req.sku(), req.price());
            Product saved = repo.save(p);
            log.info("Product created id={}, sku={}", saved.getId(), saved.getSku());
            return toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("SKU already exists: " + req.sku());
        }
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest req) {
        Product p = repo.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
        p.setName(req.name());
        p.setSku(req.sku());
        p.setPrice(req.price());
        try {
            Product saved = repo.save(p);
            log.info("Product updated id={}, sku={}", saved.getId(), saved.getSku());
            return toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("SKU already exists: " + req.sku());
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("Product not found: " + id);
        repo.deleteById(id);
        log.info("Product deleted id={}", id);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return repo.existsById(id);
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getSku(), p.getPrice());
    }
}
