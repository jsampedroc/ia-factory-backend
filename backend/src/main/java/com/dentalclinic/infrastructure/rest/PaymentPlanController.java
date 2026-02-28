package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.PaymentPlanService;
import com.dentalclinic.infrastructure.rest.dto.PaymentPlanRequest;
import com.dentalclinic.infrastructure.rest.dto.PaymentPlanResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-plans")
@RequiredArgsConstructor
public class PaymentPlanController {

    private final PaymentPlanService paymentPlanService;

    @PostMapping
    public ResponseEntity<PaymentPlanResponse> createPaymentPlan(@Valid @RequestBody PaymentPlanRequest request) {
        PaymentPlanResponse created = paymentPlanService.createPaymentPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentPlanResponse> getPaymentPlanById(@PathVariable UUID id) {
        PaymentPlanResponse response = paymentPlanService.getPaymentPlanById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentPlanResponse>> getPaymentPlansByInvoiceId(@PathVariable UUID invoiceId) {
        List<PaymentPlanResponse> response = paymentPlanService.getPaymentPlansByInvoiceId(invoiceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentPlanResponse> updatePaymentPlan(@PathVariable UUID id,
                                                                 @Valid @RequestBody PaymentPlanRequest request) {
        PaymentPlanResponse updated = paymentPlanService.updatePaymentPlan(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentPlan(@PathVariable UUID id) {
        paymentPlanService.deletePaymentPlan(id);
        return ResponseEntity.noContent().build();
    }
}