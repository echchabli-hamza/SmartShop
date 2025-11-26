package com.SmartShop.SmartShop.exception.customeExaptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
