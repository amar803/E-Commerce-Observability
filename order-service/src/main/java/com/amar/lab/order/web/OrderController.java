package com.amar.lab.order.web;

import com.amar.lab.common.api.ApiResponse;
import com.amar.lab.common.web.TraceIdFilter;
import com.amar.lab.order.service.OrderService;
import com.amar.lab.order.web.dto.CreateOrderRequest;
import com.amar.lab.order.web.dto.OrderResponse;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> list() {
        return ApiResponse.ok("ok", service.list(), MDC.get(TraceIdFilter.TRACE_ID));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> get(@PathVariable Long id) {
        return ApiResponse.ok("ok", service.get(id), MDC.get(TraceIdFilter.TRACE_ID));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        return ApiResponse.ok("created", service.create(req), MDC.get(TraceIdFilter.TRACE_ID));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
