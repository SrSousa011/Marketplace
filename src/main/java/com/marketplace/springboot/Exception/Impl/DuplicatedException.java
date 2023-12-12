package com.marketplace.springboot.Exception.Impl;

import java.util.UUID;
public class DuplicatedException extends RuntimeException {


    public DuplicatedException(String entity, UUID id) {
        super(String.format("%s with ID %s already exists.", entity, id));
    }

    public DuplicatedException(String entity) {

        super(String.format("%s already exists.", entity));
    }
}