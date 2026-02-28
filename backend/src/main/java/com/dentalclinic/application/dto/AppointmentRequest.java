package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import com.dentalclinic.domain.shared.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull(message = "Patient ID is required")
        UUID patientId,

        @NotNull(message = "Dentist ID is required")
        UUID dentistId,

        @NotNull(message = "Scheduled time is required")
        @FutureOrPresent(message = "Scheduled time must be in the present or future")
        LocalDateTime scheduledTime,

        @NotNull(message = "Status is required")
        AppointmentStatus status
) {}