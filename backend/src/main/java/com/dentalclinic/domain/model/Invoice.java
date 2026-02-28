package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.InvoiceId;
import com.dentalclinic.domain.valueobject.PaymentPlanId;
import com.dentalclinic.domain.valueobject.InsuranceClaimId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class Invoice extends Entity<InvoiceId> {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private List<PaymentPlan> paymentPlans;

    private InsuranceClaim insuranceClaim;
}