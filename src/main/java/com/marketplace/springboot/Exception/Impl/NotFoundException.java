package com.marketplace.springboot.Exception.Impl;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entity, UUID id) {
        super(String.format("%s with ID %s" + id + " has been not found.", entity, id));
    }
    public NotFoundException(String entity) {

        super(String.format("%s has been not found.", entity));
    }
}

