package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.PaymentPlanRequest;
import com.dentalclinic.application.dto.PaymentPlanResponse;
import com.dentalclinic.domain.model.Invoice;
import com.dentalclinic.domain.model.PaymentPlan;
import com.dentalclinic.domain.valueobject.InvoiceId;
import com.dentalclinic.domain.valueobject.PaymentPlanId;
import com.dentalclinic.domain.repository.InvoiceRepository;
import com.dentalclinic.domain.repository.PaymentPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentPlanService {

    private final PaymentPlanRepository paymentPlanRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public PaymentPlanResponse createPaymentPlan(PaymentPlanRequest request) {
        log.info("Creating payment plan for invoice ID: {}", request.invoiceId());

        Invoice invoice = invoiceRepository.findById(new InvoiceId(UUID.fromString(request.invoiceId())))
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + request.invoiceId()));

        validatePaymentPlanAmount(request, invoice);

        PaymentPlan paymentPlan = PaymentPlan.builder()
                .installmentAmount(request.installmentAmount())
                .numberOfInstallments(request.numberOfInstallments())
                .build();

        invoice.addPaymentPlan(paymentPlan);
        invoiceRepository.save(invoice);

        log.info("Payment plan created with ID: {}", paymentPlan.getId().value());
        return mapToResponse(paymentPlan);
    }

    public PaymentPlanResponse getPaymentPlanById(String id) {
        log.info("Fetching payment plan with ID: {}", id);
        PaymentPlan paymentPlan = paymentPlanRepository.findById(new PaymentPlanId(UUID.fromString(id)))
                .orElseThrow(() -> new IllegalArgumentException("Payment plan not found with ID: " + id));
        return mapToResponse(paymentPlan);
    }

    public List<PaymentPlanResponse> getPaymentPlansByInvoiceId(String invoiceId) {
        log.info("Fetching payment plans for invoice ID: {}", invoiceId);
        List<PaymentPlan> paymentPlans = paymentPlanRepository.findByInvoiceId(new InvoiceId(UUID.fromString(invoiceId)));
        return paymentPlans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentPlanResponse updatePaymentPlan(String id, PaymentPlanRequest request) {
        log.info("Updating payment plan with ID: {}", id);
        PaymentPlan paymentPlan = paymentPlanRepository.findById(new PaymentPlanId(UUID.fromString(id)))
                .orElseThrow(() -> new IllegalArgumentException("Payment plan not found with ID: " + id));

        Invoice invoice = invoiceRepository.findByPaymentPlanId(paymentPlan.getId())
                .orElseThrow(() -> new IllegalStateException("Associated invoice not found for payment plan ID: " + id));

        BigDecimal oldTotal = paymentPlan.getInstallmentAmount().multiply(BigDecimal.valueOf(paymentPlan.getNumberOfInstallments()));
        BigDecimal newTotal = request.installmentAmount().multiply(BigDecimal.valueOf(request.numberOfInstallments()));
        BigDecimal invoiceAmountDifference = newTotal.subtract(oldTotal);

        if (invoice.getAmount().compareTo(invoice.getTotalPaymentPlanAmount().add(invoiceAmountDifference)) != 0) {
            throw new IllegalArgumentException("Updated payment plan total does not match invoice amount.");
        }

        paymentPlan.setInstallmentAmount(request.installmentAmount());
        paymentPlan.setNumberOfInstallments(request.numberOfInstallments());

        PaymentPlan updatedPlan = paymentPlanRepository.save(paymentPlan);
        log.info("Payment plan updated with ID: {}", updatedPlan.getId().value());
        return mapToResponse(updatedPlan);
    }

    @Transactional
    public void deletePaymentPlan(String id) {
        log.info("Deleting payment plan with ID: {}", id);
        PaymentPlan paymentPlan = paymentPlanRepository.findById(new PaymentPlanId(UUID.fromString(id)))
                .orElseThrow(() -> new IllegalArgumentException("Payment plan not found with ID: " + id));

        Invoice invoice = invoiceRepository.findByPaymentPlanId(paymentPlan.getId())
                .orElseThrow(() -> new IllegalStateException("Associated invoice not found for payment plan ID: " + id));

        invoice.removePaymentPlan(paymentPlan);
        invoiceRepository.save(invoice);
        paymentPlanRepository.delete(paymentPlan);
        log.info("Payment plan deleted with ID: {}", id);
    }

    private void validatePaymentPlanAmount(PaymentPlanRequest request, Invoice invoice) {
        BigDecimal newPlanTotal = request.installmentAmount().multiply(BigDecimal.valueOf(request.numberOfInstallments()));
        BigDecimal existingPlansTotal = invoice.getTotalPaymentPlanAmount();
        BigDecimal proposedTotal = existingPlansTotal.add(newPlanTotal);

        if (proposedTotal.compareTo(invoice.getAmount()) != 0) {
            throw new IllegalArgumentException(
                    String.format("Payment plan total (%.2f) does not match invoice amount (%.2f). Existing plans total: %.2f",
                            proposedTotal, invoice.getAmount(), existingPlansTotal)
            );
        }
    }

    private PaymentPlanResponse mapToResponse(PaymentPlan paymentPlan) {
        return new PaymentPlanResponse(
                paymentPlan.getId().value().toString(),
                paymentPlan.getInstallmentAmount(),
                paymentPlan.getNumberOfInstallments()
        );
    }
}