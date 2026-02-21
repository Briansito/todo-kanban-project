package com.todokanban.infrastructure.adapter.in.rest.dto;

/**
 * Request DTO for updating a Card's text fields.
 * Both fields are optional — null means "no change".
 */
public record UpdateCardRequest(String title, String description) {}
