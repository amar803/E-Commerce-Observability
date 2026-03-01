package com.amar.lab.order.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items", indexes = {
        @Index(name = "idx_order_items_order_id", columnList = "order_id")
})
public class OrderItemEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    public Long getId() { return id; }
    public OrderEntity getOrder() { return order; }
    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }

    public void setId(Long id) { this.id = id; }
    public void setOrder(OrderEntity order) { this.order = order; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
