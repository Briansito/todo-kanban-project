package com.todokanban.infrastructure.adapter.in.rest.dto;

/** Request DTO for creating a Card inside a Column. */
public record CreateCardRequest(String title, String description) {
    public CreateCardRequest {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required and must not be blank");
        }
    }
}
