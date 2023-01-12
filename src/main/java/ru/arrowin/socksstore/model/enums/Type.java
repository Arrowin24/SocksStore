package ru.arrowin.socksstore.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {
    ADD("Приемка"), SELL("Выдача"), DELETE("Списание");
    private final String description;

    Type(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
