package com.amar.lab.common.api;

import java.time.Instant;

public record ApiResponse<T>(
        Instant timestamp,
        boolean success,
        String message,
        T data,
        String traceId
) {
    public static <T> ApiResponse<T> ok(String message, T data, String traceId) {
        return new ApiResponse<>(Instant.now(), true, message, data, traceId);
    }
}
