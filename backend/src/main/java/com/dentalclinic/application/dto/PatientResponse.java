package com.dentalclinic.application.dto;

import com.dentalclinic.domain.model.*;
import com.dentalclinic.domain.valueobject.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PatientResponse(
        @NotNull UUID id,
        @NotNull @Size(min = 1, max = 255) String name,
        @NotNull LocalDate dateOfBirth,
        @NotNull ProfileResponse profile,
        @NotNull List<ElectronicHealthRecordResponse> healthRecords,
        @NotNull List<AppointmentResponse> appointments,
        @NotNull List<InvoiceResponse> invoices
) {
    public record ProfileResponse(
            @NotNull UUID id,
            @NotNull List<MedicalAlertResponse> medicalAlerts,
            @NotNull List<AllergyResponse> allergies,
            @NotNull List<ConsentResponse> consents
    ) {}

    public record MedicalAlertResponse(
            @NotNull UUID id,
            @NotNull @Size(min = 1, max = 1000) String description,
            @NotNull LocalDate dateIssued
    ) {}

    public record AllergyResponse(
            @NotNull UUID id,
            @NotNull @Size(min = 1, max = 255) String allergen,
            @NotNull @Size(min = 1, max = 1000) String reaction
    ) {}

    public record ConsentResponse(
            @NotNull UUID id,
            @NotNull @Size(min = 1, max = 1000) String description,
            @NotNull LocalDate dateGiven
    ) {}

    public record ElectronicHealthRecordResponse(
            @NotNull UUID id,
            @NotNull List<ClinicalNoteResponse> clinicalNotes,
            @NotNull OdontogramResponse odontogram,
            @NotNull List<TreatmentAuditLogResponse> treatmentAuditLogs
    ) {}

    public record ClinicalNoteResponse(
            @NotNull UUID id,
            @NotNull @Size(min = 1, max = 5000) String note,
            @NotNull LocalDateTime timestamp
    ) {}

    public record OdontogramResponse(
            @NotNull UUID id,
            @NotNull String diagram
    ) {}

    public record TreatmentAuditLogResponse(
            @NotNull UUID id,
            @NotNull @Size(min = 1, max = 5000) String treatmentDetails,
            @NotNull LocalDateTime timestamp
    ) {}

    public record AppointmentResponse(
            @NotNull UUID id,
            @NotNull LocalDateTime scheduledTime,
            @NotNull com.dentalclinic.domain.shared.AppointmentStatus status,
            @NotNull DentistResponse dentist
    ) {}

    public record DentistResponse(
            @NotNull UUID id,
            @NotNull @Size(min = 1, max = 255) String name
    ) {}

    public record InvoiceResponse(
            @NotNull UUID id,
            @NotNull BigDecimal amount,
            @NotNull List<PaymentPlanResponse> paymentPlans,
            @NotNull InsuranceClaimResponse insuranceClaim
    ) {}

    public record PaymentPlanResponse(
            @NotNull UUID id,
            @NotNull BigDecimal installmentAmount,
            @NotNull int numberOfInstallments
    ) {}

    public record InsuranceClaimResponse(
            @NotNull UUID id,
            @NotNull @Size(min = 1, max = 100) String claimNumber,
            @NotNull BigDecimal claimedAmount
    ) {}
}