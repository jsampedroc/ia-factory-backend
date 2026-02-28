package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.InsuranceClaimId;
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
public class InsuranceClaimResponse {
    @NotNull
    private UUID id;

    @NotBlank
    private String claimNumber;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal claimedAmount;
}