package com.amar.lab.order.service;

import com.amar.lab.order.domain.OrderEntity;
import com.amar.lab.order.domain.OrderItemEntity;
import com.amar.lab.order.repo.OrderRepository;
import com.amar.lab.order.web.dto.CreateOrderRequest;
import com.amar.lab.order.web.dto.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repo;
    private final ProductClient productClient;

    public OrderService(OrderRepository repo, ProductClient productClient) {
        this.repo = repo;
        this.productClient = productClient;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest req) {
        for (CreateOrderRequest.Item item : req.items()) {
            if (!productClient.productExists(item.productId())) {
                throw new BadRequestException("Invalid productId: " + item.productId());
            }
        }

        OrderEntity order = new OrderEntity();
        order.setCustomerName(req.customerName());

        for (CreateOrderRequest.Item item : req.items()) {
            OrderItemEntity oi = new OrderItemEntity();
            oi.setOrder(order);
            oi.setProductId(item.productId());
            oi.setQuantity(item.quantity());
            order.getItems().add(oi);
        }

        OrderEntity saved = repo.save(order);
        log.info("Order created id={}, customer={}", saved.getId(), saved.getCustomerName());
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("Order not found: " + id);
        repo.deleteById(id);
        log.info("Order deleted id={}", id);
    }

    private OrderResponse toResponse(OrderEntity o) {
        return new OrderResponse(
                o.getId(),
                o.getCustomerName(),
                o.getCreatedAt(),
                o.getItems().stream().map(i -> new OrderResponse.Item(i.getProductId(), i.getQuantity())).toList()
        );
    }
}
