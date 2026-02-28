package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.DentistId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DentistResponse {
    @NotNull
    private DentistId id;

    @NotBlank
    private String name;

    private List<UUID> appointmentIds; // Representing the list of associated Appointment IDs
}