package com.dentalclinic.application.service;

import com.dentalclinic.domain.model.Invoice;
import com.dentalclinic.domain.model.Patient;
import com.dentalclinic.domain.model.PaymentPlan;
import com.dentalclinic.domain.model.InsuranceClaim;
import com.dentalclinic.domain.valueobject.InvoiceId;
import com.dentalclinic.domain.valueobject.PatientId;
import com.dentalclinic.domain.valueobject.PaymentPlanId;
import com.dentalclinic.domain.valueobject.InsuranceClaimId;
import com.dentalclinic.application.dto.InvoiceRequest;
import com.dentalclinic.application.dto.InvoiceResponse;
import com.dentalclinic.application.dto.PaymentPlanRequest;
import com.dentalclinic.application.dto.InsuranceClaimRequest;
import com.dentalclinic.domain.repository.InvoiceRepository;
import com.dentalclinic.domain.repository.PatientRepository;
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
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        PatientId patientId = new PatientId(request.patientId());
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + request.patientId()));

        Invoice invoice = Invoice.builder()
                .id(new InvoiceId(UUID.randomUUID()))
                .amount(request.amount())
                .build();

        patient.addInvoice(invoice);
        patientRepository.save(patient);

        log.info("Created invoice with ID: {} for patient ID: {}", invoice.getId().value(), patientId.value());
        return mapToResponse(invoice);
    }

    public InvoiceResponse getInvoiceById(UUID invoiceUuid) {
        InvoiceId invoiceId = new InvoiceId(invoiceUuid);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceUuid));
        return mapToResponse(invoice);
    }

    public List<InvoiceResponse> getInvoicesByPatientId(UUID patientUuid) {
        PatientId patientId = new PatientId(patientUuid);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientUuid));
        return patient.getInvoices().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InvoiceResponse addPaymentPlanToInvoice(UUID invoiceUuid, PaymentPlanRequest request) {
        InvoiceId invoiceId = new InvoiceId(invoiceUuid);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceUuid));

        PaymentPlan paymentPlan = PaymentPlan.builder()
                .id(new PaymentPlanId(UUID.randomUUID()))
                .installmentAmount(request.installmentAmount())
                .numberOfInstallments(request.numberOfInstallments())
                .build();

        invoice.addPaymentPlan(paymentPlan);
        invoiceRepository.save(invoice);

        log.info("Added payment plan to invoice ID: {}", invoiceId.value());
        return mapToResponse(invoice);
    }

    @Transactional
    public InvoiceResponse addInsuranceClaimToInvoice(UUID invoiceUuid, InsuranceClaimRequest request) {
        InvoiceId invoiceId = new InvoiceId(invoiceUuid);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceUuid));

        if (invoice.getInsuranceClaim() != null) {
            throw new IllegalStateException("Invoice already has an insurance claim.");
        }

        InsuranceClaim insuranceClaim = InsuranceClaim.builder()
                .id(new InsuranceClaimId(UUID.randomUUID()))
                .claimNumber(request.claimNumber())
                .claimedAmount(request.claimedAmount())
                .build();

        invoice.setInsuranceClaim(insuranceClaim);
        invoiceRepository.save(invoice);

        log.info("Added insurance claim to invoice ID: {}", invoiceId.value());
        return mapToResponse(invoice);
    }

    @Transactional
    public void validateInvoiceAmountMatchesPaymentPlans(UUID invoiceUuid) {
        InvoiceId invoiceId = new InvoiceId(invoiceUuid);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceUuid));

        BigDecimal totalFromPlans = invoice.getPaymentPlans().stream()
                .map(plan -> plan.getInstallmentAmount().multiply(BigDecimal.valueOf(plan.getNumberOfInstallments())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (invoice.getAmount().compareTo(totalFromPlans) != 0) {
            throw new IllegalStateException(
                    String.format("Invoice amount %s does not match sum of payment plans %s.",
                            invoice.getAmount(), totalFromPlans)
            );
        }
        log.info("Invoice amount validation passed for invoice ID: {}", invoiceId.value());
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        List<PaymentPlanRequest> paymentPlans = invoice.getPaymentPlans().stream()
                .map(plan -> new PaymentPlanRequest(
                        plan.getId().value(),
                        plan.getInstallmentAmount(),
                        plan.getNumberOfInstallments()
                ))
                .collect(Collectors.toList());

        InsuranceClaimRequest insuranceClaim = null;
        if (invoice.getInsuranceClaim() != null) {
            InsuranceClaim claim = invoice.getInsuranceClaim();
            insuranceClaim = new InsuranceClaimRequest(
                    claim.getId().value(),
                    claim.getClaimNumber(),
                    claim.getClaimedAmount()
            );
        }

        return new InvoiceResponse(
                invoice.getId().value(),
                invoice.getAmount(),
                paymentPlans,
                insuranceClaim
        );
    }
}