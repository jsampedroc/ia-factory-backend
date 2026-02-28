package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import com.dentalclinic.domain.valueobject.OdontogramId;
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
public class ElectronicHealthRecord extends Entity<ElectronicHealthRecordId> {

    @NotNull
    @SuperBuilder.Default
    private List<ClinicalNote> clinicalNotes = new ArrayList<>();

    @NotNull
    private Odontogram odontogram;

    @NotNull
    @SuperBuilder.Default
    private List<TreatmentAuditLog> treatmentAuditLogs = new ArrayList<>();
}