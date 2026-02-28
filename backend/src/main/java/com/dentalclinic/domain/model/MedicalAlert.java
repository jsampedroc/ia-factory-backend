package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.MedicalAlertId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class MedicalAlert extends Entity<MedicalAlertId> {

    @NotBlank(message = "Medical alert description cannot be blank")
    private String description;

    @NotNull(message = "Date issued is required")
    @PastOrPresent(message = "Date issued cannot be in the future")
    private LocalDate dateIssued;
}