package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergyRequest {

    @NotBlank(message = "Allergen is mandatory")
    @Size(max = 255, message = "Allergen cannot exceed 255 characters")
    private String allergen;

    @NotBlank(message = "Reaction is mandatory")
    @Size(max = 500, message = "Reaction cannot exceed 500 characters")
    private String reaction;
}