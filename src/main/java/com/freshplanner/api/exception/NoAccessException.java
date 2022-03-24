package com.freshplanner.api.exception;

import lombok.Getter;

public class NoAccessException extends Exception {

    @Getter
    private final String username;
    @Getter
    private final String entityClass;
    @Getter
    private final String id;

    public NoAccessException(String username, Class<?> entityClass, String id) {
        super("User (" + username + ") has no access to " + entityClass.getSimpleName() + " (" + id + ")");
        this.username = username;
        this.entityClass = entityClass.getSimpleName();
        this.id = id;
    }
}
