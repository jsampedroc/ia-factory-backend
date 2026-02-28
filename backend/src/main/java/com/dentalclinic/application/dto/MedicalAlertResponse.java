package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.MedicalAlertId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAlertResponse {
    @NotNull
    private UUID id;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate dateIssued;
}