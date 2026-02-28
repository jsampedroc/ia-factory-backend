package com.dentalclinic.application.dto;

import com.dentalclinic.domain.model.ClinicalNote;
import com.dentalclinic.domain.model.Odontogram;
import com.dentalclinic.domain.model.TreatmentAuditLog;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicHealthRecordResponse {
    @NotNull
    private ElectronicHealthRecordId id;
    private List<ClinicalNote> clinicalNotes;
    private Odontogram odontogram;
    private List<TreatmentAuditLog> treatmentAuditLogs;
}