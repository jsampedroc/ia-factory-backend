package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.TreatmentAuditLogId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TreatmentAuditLog extends Entity<TreatmentAuditLogId> {

    @NotBlank(message = "Treatment details cannot be blank")
    @Size(max = 2000, message = "Treatment details cannot exceed 2000 characters")
    private String treatmentDetails;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

}