package com.todokanban.infrastructure.adapter.in.rest.dto;

/** Request DTO for adding a Column to a Board. */
public record CreateColumnRequest(String name, int position) {
    public CreateColumnRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required and must not be blank");
        }
        if (position < 0) {
            throw new IllegalArgumentException("position must be non-negative");
        }
    }
}
