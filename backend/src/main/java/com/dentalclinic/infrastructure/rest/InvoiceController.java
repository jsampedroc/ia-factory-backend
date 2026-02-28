package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.InvoiceService;

//IMPORT GENERADO PARA DTOs DE REQUEST Y RESPONSE
//import com.dentalclinic.infrastructure.rest.dto.InvoiceRequest;
//import com.dentalclinic.infrastructure.rest.dto.InvoiceRequest;

//IMPORT CORREGIDO PARA DTOs DE REQUEST Y RESPONSE
import com.dentalclinic.application.dto.InvoiceResponse;
import com.dentalclinic.application.dto.InvoiceRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse createdInvoice = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        InvoiceResponse invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByPatientId(@PathVariable UUID patientId) {
        List<InvoiceResponse> invoices = invoiceService.getInvoicesByPatientId(patientId);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable UUID id,
                                                         @Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse updatedInvoice = invoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(updatedInvoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{invoiceId}/payment-plans")
    public ResponseEntity<InvoiceResponse> addPaymentPlan(@PathVariable UUID invoiceId,
                                                          @Valid @RequestBody PaymentPlanRequest request) {
        InvoiceResponse updatedInvoice = invoiceService.addPaymentPlan(invoiceId, request);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PostMapping("/{invoiceId}/insurance-claim")
    public ResponseEntity<InvoiceResponse> attachInsuranceClaim(@PathVariable UUID invoiceId,
                                                                @Valid @RequestBody InsuranceClaimRequest request) {
        InvoiceResponse updatedInvoice = invoiceService.attachInsuranceClaim(invoiceId, request);
        return ResponseEntity.ok(updatedInvoice);
    }
}