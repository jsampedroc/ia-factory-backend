package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.ProfileId;
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
public class ProfileResponse {
    @NotNull
    private UUID id;
    private List<MedicalAlertResponse> medicalAlerts;
    private List<AllergyResponse> allergies;
    private List<ConsentResponse> consents;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicalAlertResponse {
        private UUID id;
        private String description;
        private LocalDate dateIssued;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllergyResponse {
        private UUID id;
        private String allergen;
        private String reaction;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsentResponse {
        private UUID id;
        private String description;
        private LocalDate dateGiven;
    }
}