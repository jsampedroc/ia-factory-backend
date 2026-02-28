package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;

public record MedicalAlertRequest(
        @NotNull(message = "Profile ID is required")
        UUID profileId,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Date issued is required")
        @PastOrPresent(message = "Date issued must be in the past or present")
        LocalDate dateIssued
) {}