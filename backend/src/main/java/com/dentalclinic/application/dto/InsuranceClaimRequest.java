package com.dentalclinic.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceClaimRequest {

    @NotBlank(message = "Claim number is mandatory")
    private String claimNumber;

    @NotNull(message = "Claimed amount is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Claimed amount must be greater than zero")
    private BigDecimal claimedAmount;

    private UUID invoiceId;
}