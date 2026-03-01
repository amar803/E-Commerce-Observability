package com.amar.lab.order.service;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
