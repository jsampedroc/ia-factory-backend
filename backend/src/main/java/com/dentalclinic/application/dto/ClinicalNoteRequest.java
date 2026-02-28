package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClinicalNoteRequest(
        @NotNull(message = "EHR ID is mandatory")
        UUID ehrId,

        @NotBlank(message = "Note content cannot be blank")
        @Size(max = 2000, message = "Note cannot exceed 2000 characters")
        String note,

        @NotNull(message = "Timestamp is mandatory")
        LocalDateTime timestamp
) {}