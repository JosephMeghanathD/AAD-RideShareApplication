package com.ride.share.aad.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * A record representing the data structure for a contact form submission.
 * The annotations are used by Spring's validation framework.
 */
public record ContactRequest(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "Subject cannot be empty")
        String subject,

        @NotBlank(message = "Message cannot be empty")
        @Size(min = 10, message = "Message must be at least 10 characters long")
        String message
) {}