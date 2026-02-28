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
public class DentistRequest {
    @NotBlank(message = "Dentist name is mandatory")
    @Size(max = 255, message = "Dentist name must not exceed 255 characters")
    private String name;
}