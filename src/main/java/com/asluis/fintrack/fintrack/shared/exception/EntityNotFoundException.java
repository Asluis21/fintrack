package com.asluis.fintrack.fintrack.shared.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Class<?> entityType, Object id) {
        super(entityType.getSimpleName() + " not found with id: " + id);
    }
}
