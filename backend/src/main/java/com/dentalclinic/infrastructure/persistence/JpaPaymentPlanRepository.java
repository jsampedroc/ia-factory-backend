package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.PaymentPlan;
import com.dentalclinic.domain.valueobject.PaymentPlanId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaPaymentPlanRepository extends JpaRepository<PaymentPlan, UUID> {
}