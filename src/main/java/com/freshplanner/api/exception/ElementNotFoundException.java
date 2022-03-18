package com.freshplanner.api.exception;

import lombok.Getter;

import javax.persistence.Table;

public class ElementNotFoundException extends Exception {

    @Getter
    private final String id;
    @Getter
    private final String table;

    public ElementNotFoundException(Class<?> entityClass, String id) {
        super("Element in table ('" + entityClass.getAnnotation(Table.class).name() + "') with ID ('" + id + "') not found.");
        this.table = entityClass.getAnnotation(Table.class).name();
        this.id = id;
    }
}
