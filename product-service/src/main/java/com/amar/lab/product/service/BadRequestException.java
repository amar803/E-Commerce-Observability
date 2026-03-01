package com.amar.lab.product.service;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
