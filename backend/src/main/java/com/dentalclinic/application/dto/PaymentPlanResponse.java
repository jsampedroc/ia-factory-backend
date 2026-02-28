package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.PaymentPlanId;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record PaymentPlanResponse(
        @NotNull
        UUID id,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal installmentAmount,

        @NotNull
        @Min(1)
        int numberOfInstallments
) {
    public static PaymentPlanResponse fromDomain(PaymentPlanId id, BigDecimal installmentAmount, int numberOfInstallments) {
        return new PaymentPlanResponse(id.value(), installmentAmount, numberOfInstallments);
    }
}