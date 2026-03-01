package com.amar.lab.product.web;

import com.amar.lab.common.api.ApiError;
import com.amar.lab.common.web.TraceIdFilter;
import com.amar.lab.product.service.BadRequestException;
import com.amar.lab.product.service.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFound(NotFoundException ex, HttpServletRequest req) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiError> badRequest(Exception ex, HttpServletRequest req) {
        String msg = ex instanceof MethodArgumentNotValidException manv
                ? manv.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("validation error")
                : ex.getMessage();
        return error(HttpStatus.BAD_REQUEST, msg, req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> internal(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req.getRequestURI());
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message, String path) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                MDC.get(TraceIdFilter.TRACE_ID)
        );
        return ResponseEntity.status(status).body(body);
    }
}
