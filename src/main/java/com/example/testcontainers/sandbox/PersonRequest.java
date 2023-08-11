package com.example.testcontainers.sandbox;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

public record PersonRequest(
    @NotNull(message = "First name can not be null.") String firstName,
    @NotNull(message = "Last name can not be null.") String lastName,
    @Pattern(
            regexp = "CH" + "|BR" + "|PT" + "|FR",
            message = "Supported values for country field:" + "CH" + "|BR" + "|FR" + "|PT.")
        String country,
    @Pattern(
            regexp = "DE" + "|IT" + "|FR" + "|EN" + "|PT",
            message =
                "Supported values for language field:" + "DE" + "|IT" + "|FR" + "|EN" + "|PT.")
        String language)
    implements Serializable {}
