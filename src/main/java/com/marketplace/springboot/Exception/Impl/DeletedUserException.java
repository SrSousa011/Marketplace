package com.marketplace.springboot.Exception.Impl;


public class DeletedUserException extends Throwable {

    public DeletedUserException(String message) {
        super(message);
    }
}