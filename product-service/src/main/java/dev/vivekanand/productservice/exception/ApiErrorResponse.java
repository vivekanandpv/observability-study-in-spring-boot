package dev.vivekanand.productservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        int status,
        String error,
        List<String> messages,
        String path,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp
) { }
