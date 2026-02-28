package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.TreatmentAuditLogId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

public record TreatmentAuditLogResponse(
        @NotNull
        UUID id,

        @NotBlank
        @Size(max = 2000)
        String treatmentDetails,

        @NotNull
        LocalDateTime timestamp
) {
    public static TreatmentAuditLogResponse fromIdAndDetails(TreatmentAuditLogId id, String treatmentDetails, LocalDateTime timestamp) {
        return new TreatmentAuditLogResponse(id.value(), treatmentDetails, timestamp);
    }
}