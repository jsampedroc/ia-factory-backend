package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record ConsentRequest(
        @NotNull(message = "Profile ID is required")
        UUID profileId,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Date given is required")
        LocalDate dateGiven
) {}