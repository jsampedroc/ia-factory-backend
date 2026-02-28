package com.dentalclinic.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class InvoiceRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must have up to 10 integer and 2 fraction digits")
    private BigDecimal amount;

    private List<PaymentPlanRequest> paymentPlans;

    private InsuranceClaimRequest insuranceClaim;

    // Constructors
    public InvoiceRequest() {
    }

    public InvoiceRequest(UUID patientId, BigDecimal amount, List<PaymentPlanRequest> paymentPlans, InsuranceClaimRequest insuranceClaim) {
        this.patientId = patientId;
        this.amount = amount;
        this.paymentPlans = paymentPlans;
        this.insuranceClaim = insuranceClaim;
    }

    // Getters and Setters
    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<PaymentPlanRequest> getPaymentPlans() {
        return paymentPlans;
    }

    public void setPaymentPlans(List<PaymentPlanRequest> paymentPlans) {
        this.paymentPlans = paymentPlans;
    }

    public InsuranceClaimRequest getInsuranceClaim() {
        return insuranceClaim;
    }

    public void setInsuranceClaim(InsuranceClaimRequest insuranceClaim) {
        this.insuranceClaim = insuranceClaim;
    }

    // Nested DTO for PaymentPlan
    public static class PaymentPlanRequest {
        @NotNull(message = "Installment amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Installment amount must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Installment amount must have up to 10 integer and 2 fraction digits")
        private BigDecimal installmentAmount;

        @NotNull(message = "Number of installments is required")
        @Min(value = 1, message = "Number of installments must be at least 1")
        private Integer numberOfInstallments;

        public PaymentPlanRequest() {
        }

        public PaymentPlanRequest(BigDecimal installmentAmount, Integer numberOfInstallments) {
            this.installmentAmount = installmentAmount;
            this.numberOfInstallments = numberOfInstallments;
        }

        public BigDecimal getInstallmentAmount() {
            return installmentAmount;
        }

        public void setInstallmentAmount(BigDecimal installmentAmount) {
            this.installmentAmount = installmentAmount;
        }

        public Integer getNumberOfInstallments() {
            return numberOfInstallments;
        }

        public void setNumberOfInstallments(Integer numberOfInstallments) {
            this.numberOfInstallments = numberOfInstallments;
        }
    }

    // Nested DTO for InsuranceClaim
    public static class InsuranceClaimRequest {
        @NotBlank(message = "Claim number is required")
        @Size(max = 50, message = "Claim number must not exceed 50 characters")
        private String claimNumber;

        @NotNull(message = "Claimed amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Claimed amount must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Claimed amount must have up to 10 integer and 2 fraction digits")
        private BigDecimal claimedAmount;

        public InsuranceClaimRequest() {
        }

        public InsuranceClaimRequest(String claimNumber, BigDecimal claimedAmount) {
            this.claimNumber = claimNumber;
            this.claimedAmount = claimedAmount;
        }

        public String getClaimNumber() {
            return claimNumber;
        }

        public void setClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
        }

        public BigDecimal getClaimedAmount() {
            return claimedAmount;
        }

        public void setClaimedAmount(BigDecimal claimedAmount) {
            this.claimedAmount = claimedAmount;
        }
    }
}