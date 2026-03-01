package com.amar.lab.common.api;

import java.time.Instant;
import org.slf4j.MDC;
import com.amar.lab.common.web.TraceIdFilter;

public record ApiResponse<T>(
        Instant timestamp,
        boolean success,
        String message,
        T data,
        String traceId
) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        String traceId = MDC.get(TraceIdFilter.TRACE_ID);
        if (traceId == null) traceId = "n/a";

        return new ApiResponse<>(
                Instant.now(),
                true,
                message,
                data,
                traceId
        );
    }
}


