package com.example.testcontainers.sandbox;

import java.io.Serializable;
import java.util.UUID;

public record PersonResponse(
    UUID id, String firstName, String lastName, String country, String language)
    implements Serializable {}
