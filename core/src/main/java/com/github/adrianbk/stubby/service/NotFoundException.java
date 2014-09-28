package com.github.adrianbk.stubby.service;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
    
}
