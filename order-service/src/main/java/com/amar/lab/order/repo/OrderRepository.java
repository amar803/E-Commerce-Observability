package com.amar.lab.order.repo;

import com.amar.lab.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
