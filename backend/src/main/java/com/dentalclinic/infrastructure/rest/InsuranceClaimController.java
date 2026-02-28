package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.InsuranceClaimService;
import com.dentalclinic.infrastructure.rest.dto.InsuranceClaimRequest;
import com.dentalclinic.infrastructure.rest.dto.InsuranceClaimResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/insurance-claims")
@RequiredArgsConstructor
public class InsuranceClaimController {

    private final InsuranceClaimService insuranceClaimService;

    @PostMapping
    public ResponseEntity<InsuranceClaimResponse> create(@Valid @RequestBody InsuranceClaimRequest request) {
        InsuranceClaimResponse created = insuranceClaimService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceClaimResponse> getById(@PathVariable UUID id) {
        InsuranceClaimResponse response = insuranceClaimService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InsuranceClaimResponse>> getAll() {
        List<InsuranceClaimResponse> allClaims = insuranceClaimService.findAll();
        return ResponseEntity.ok(allClaims);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsuranceClaimResponse> update(@PathVariable UUID id,
                                                         @Valid @RequestBody InsuranceClaimRequest request) {
        InsuranceClaimResponse updated = insuranceClaimService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        insuranceClaimService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<InsuranceClaimResponse> getByInvoiceId(@PathVariable UUID invoiceId) {
        InsuranceClaimResponse response = insuranceClaimService.findByInvoiceId(invoiceId);
        return ResponseEntity.ok(response);
    }
}