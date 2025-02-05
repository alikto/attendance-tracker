package com.example.server.factory;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    ADMIN("ADMIN");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
