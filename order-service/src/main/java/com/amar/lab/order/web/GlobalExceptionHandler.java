package com.amar.lab.order.web;

import com.amar.lab.common.api.ApiError;
import com.amar.lab.common.web.TraceIdFilter;
import com.amar.lab.order.service.BadRequestException;
import com.amar.lab.order.service.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFound(NotFoundException ex, HttpServletRequest req) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiError> badRequest(Exception ex, HttpServletRequest req) {
        String msg = ex instanceof MethodArgumentNotValidException manv
                ? manv.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("validation error")
                : ex.getMessage();

        return error(HttpStatus.BAD_REQUEST, msg, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> internal(Exception ex, HttpServletRequest req) {
        String traceId = MDC.get(TraceIdFilter.TRACE_ID);
        // ✅ CRITICAL: log the real exception + stacktrace
        log.error("Unhandled exception traceId={}, path={}", traceId, req.getRequestURI(), ex);

        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req);
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                MDC.get(TraceIdFilter.TRACE_ID)
        );
        return ResponseEntity.status(status).body(body);
    }
}