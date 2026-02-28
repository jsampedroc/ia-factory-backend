package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.AllergyId;
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
public class AllergyResponse {
    @NotNull
    private AllergyId id;

    @NotBlank
    private String allergen;

    @NotBlank
    private String reaction;

    @NotNull
    private LocalDate dateRecorded;
}