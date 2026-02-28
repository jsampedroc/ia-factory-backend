package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.PatientId;
import com.dentalclinic.domain.valueobject.ProfileId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Patient extends Entity<PatientId> {

    @NotBlank(message = "Patient name is mandatory")
    private String name;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Profile is mandatory")
    private ProfileId profileId;

    @SuperBuilder.Default
    private List<ElectronicHealthRecord> healthRecords = new ArrayList<>();

    @SuperBuilder.Default
    private List<Appointment> appointments = new ArrayList<>();

    @SuperBuilder.Default
    private List<Invoice> invoices = new ArrayList<>();
}