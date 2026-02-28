package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.ProfileId;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Profile extends Entity<ProfileId> {

    @NotNull
    @SuperBuilder.Default
    private List<MedicalAlert> medicalAlerts = new ArrayList<>();

    @NotNull
    @SuperBuilder.Default
    private List<Allergy> allergies = new ArrayList<>();

    @NotNull
    @SuperBuilder.Default
    private List<Consent> consents = new ArrayList<>();
}