package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.AllergyId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Allergy extends Entity<AllergyId> {
    @NotBlank(message = "Allergen is mandatory")
    private String allergen;

    @NotBlank(message = "Reaction description is mandatory")
    private String reaction;

    @NotNull(message = "Date issued is mandatory")
    private LocalDate dateIssued;
}