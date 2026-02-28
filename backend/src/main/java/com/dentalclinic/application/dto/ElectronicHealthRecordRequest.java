package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class ElectronicHealthRecordRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private List<ClinicalNoteRequest> clinicalNotes;

    private OdontogramRequest odontogram;

    private List<TreatmentAuditLogRequest> treatmentAuditLogs;

    // Default constructor for deserialization
    public ElectronicHealthRecordRequest() {
    }

    // Constructor for convenience
    public ElectronicHealthRecordRequest(UUID patientId, List<ClinicalNoteRequest> clinicalNotes, OdontogramRequest odontogram, List<TreatmentAuditLogRequest> treatmentAuditLogs) {
        this.patientId = patientId;
        this.clinicalNotes = clinicalNotes;
        this.odontogram = odontogram;
        this.treatmentAuditLogs = treatmentAuditLogs;
    }

    // Getters and Setters
    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public List<ClinicalNoteRequest> getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(List<ClinicalNoteRequest> clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public OdontogramRequest getOdontogram() {
        return odontogram;
    }

    public void setOdontogram(OdontogramRequest odontogram) {
        this.odontogram = odontogram;
    }

    public List<TreatmentAuditLogRequest> getTreatmentAuditLogs() {
        return treatmentAuditLogs;
    }

    public void setTreatmentAuditLogs(List<TreatmentAuditLogRequest> treatmentAuditLogs) {
        this.treatmentAuditLogs = treatmentAuditLogs;
    }
}