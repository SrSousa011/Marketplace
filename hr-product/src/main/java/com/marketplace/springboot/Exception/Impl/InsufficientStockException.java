package com.marketplace.springboot.Exception.Impl;

public class InsufficientStockException extends RuntimeException  {

    public InsufficientStockException(String message) {

        super(message);
    }
}