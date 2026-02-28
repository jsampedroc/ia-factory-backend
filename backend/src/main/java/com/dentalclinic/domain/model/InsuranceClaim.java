package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.InsuranceClaimId;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class InsuranceClaim extends Entity<InsuranceClaimId> {

    @NotBlank(message = "Claim number cannot be blank")
    private String claimNumber;

    @NotNull(message = "Claimed amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Claimed amount must be greater than zero")
    private BigDecimal claimedAmount;
}