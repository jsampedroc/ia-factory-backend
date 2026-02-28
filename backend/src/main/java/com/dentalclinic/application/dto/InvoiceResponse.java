package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.InvoiceId;
import com.dentalclinic.domain.valueobject.PatientId;
import com.dentalclinic.domain.valueobject.InsuranceClaimId;
import com.dentalclinic.domain.model.PaymentPlan;
import com.dentalclinic.domain.model.InsuranceClaim;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvoiceResponse(
        @NotNull
        UUID id,

        @NotNull
        UUID patientId,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        LocalDateTime issuedDate,

        @NotNull
        String status,

        List<PaymentPlanResponse> paymentPlans,

        InsuranceClaimResponse insuranceClaim
) {
    public record PaymentPlanResponse(
            @NotNull
            UUID id,

            @NotNull
            @Positive
            BigDecimal installmentAmount,

            @NotNull
            @Positive
            int numberOfInstallments,

            @NotNull
            String status
    ) {}

    public record InsuranceClaimResponse(
            @NotNull
            UUID id,

            @NotNull
            String claimNumber,

            @NotNull
            @Positive
            BigDecimal claimedAmount,

            @NotNull
            String status
    ) {}
}