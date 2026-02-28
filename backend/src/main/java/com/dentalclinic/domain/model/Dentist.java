package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.DentistId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Dentist extends Entity<DentistId> {

    @NotBlank(message = "Dentist name cannot be blank")
    @Size(max = 255, message = "Dentist name must be less than 255 characters")
    private String name;

    @NotNull
    @SuperBuilder.Default
    private List<Appointment> appointments = new ArrayList<>();
}