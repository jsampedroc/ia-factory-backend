package com.dentalclinic.application.dto;

import com.dentalclinic.domain.shared.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        @NotNull UUID id,
        @NotNull LocalDateTime scheduledTime,
        @NotNull AppointmentStatus status,
        @NotNull UUID dentistId,
        @NotNull String dentistName,
        @NotNull UUID patientId,
        @NotNull String patientName
) {}