package com.todokanban.infrastructure.adapter.in.rest;

import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;

/**
 * Shared test utility: creates a Jackson 3 / Spring 7 JSON message converter
 * using the default JacksonJsonHttpMessageConverter which auto-configures an
 * ObjectMapper with java.time support built in (Jackson 3.x standard).
 */
final class TestJsonConverter {

    private TestJsonConverter() {}

    static JacksonJsonHttpMessageConverter create() {
        return new JacksonJsonHttpMessageConverter();
    }
}
