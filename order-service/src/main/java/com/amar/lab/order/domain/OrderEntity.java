package com.amar.lab.order.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public Instant getCreatedAt() { return createdAt; }
    public List<OrderItemEntity> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setItems(List<OrderItemEntity> items) { this.items = items; }
}
