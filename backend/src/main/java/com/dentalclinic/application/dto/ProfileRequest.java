package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotEmpty(message = "Medical alerts list cannot be null, but can be empty")
    private List<MedicalAlertRequest> medicalAlerts;

    @NotEmpty(message = "Allergies list cannot be null, but can be empty")
    private List<AllergyRequest> allergies;

    @NotEmpty(message = "Consents list cannot be null, but can be empty")
    private List<ConsentRequest> consents;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicalAlertRequest {
        @NotEmpty(message = "Description is required")
        private String description;

        @NotNull(message = "Date issued is required")
        private LocalDate dateIssued;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllergyRequest {
        @NotEmpty(message = "Allergen is required")
        private String allergen;

        @NotEmpty(message = "Reaction is required")
        private String reaction;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsentRequest {
        @NotEmpty(message = "Description is required")
        private String description;

        @NotNull(message = "Date given is required")
        private LocalDate dateGiven;
    }
}