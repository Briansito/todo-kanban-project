package com.todokanban.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.NoSuchElementException;

/**
 * Global exception handler for REST input adapters.
 *
 * <p>Uses Spring 6's {@link ProblemDetail} (RFC 9457 / RFC 7807) for structured
 * error responses, ensuring consistent JSON error payloads across all endpoints.</p>
 *
 * <p>Hierarchy:</p>
 * <ul>
 *   <li>{@link IllegalArgumentException} → 400 Bad Request</li>
 *   <li>{@link NoSuchElementException} → 404 Not Found</li>
 *   <li>{@link Exception} → 500 Internal Server Error</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation and business rule violations from domain or DTOs.
     * Returns {@code 400 Bad Request}.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Bad Request");
        problem.setType(URI.create("https://api.todokanban.com/errors/bad-request"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Handles malformed JSON or deserialization failures.
     *
     * <p>When a record's compact constructor throws {@link IllegalArgumentException}
     * during Jackson deserialization, Jackson wraps it in this exception.
     * We unwrap the root cause to return a user-friendly 400.</p>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        String detail = cause.getMessage() != null ? cause.getMessage() : "Malformed or invalid request body";
        log.warn("Not readable: {}", detail);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Bad Request");
        problem.setType(URI.create("https://api.todokanban.com/errors/bad-request"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Handles lookups for non-existent resources.
     * Returns {@code 404 Not Found}.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail handleNotFound(NoSuchElementException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Not Found");
        problem.setType(URI.create("https://api.todokanban.com/errors/not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Catch-all handler for unexpected errors.
     * Returns {@code 500 Internal Server Error} without leaking internal details.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.");
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("https://api.todokanban.com/errors/internal-server-error"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
