package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.InsuranceClaimRequest;
import com.dentalclinic.application.dto.InsuranceClaimResponse;
import com.dentalclinic.application.mapper.InsuranceClaimMapper;
import com.dentalclinic.domain.model.InsuranceClaim;
import com.dentalclinic.domain.model.Invoice;
import com.dentalclinic.domain.valueobject.InsuranceClaimId;
import com.dentalclinic.domain.valueobject.InvoiceId;
import com.dentalclinic.domain.repository.InsuranceClaimRepository;
import com.dentalclinic.domain.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceClaimService {

    private final InsuranceClaimRepository insuranceClaimRepository;
    private final InvoiceRepository invoiceRepository;
    private final InsuranceClaimMapper insuranceClaimMapper;

    @Transactional
    public InsuranceClaimResponse createInsuranceClaim(InsuranceClaimRequest request) {
        log.info("Creating insurance claim for invoice ID: {}", request.invoiceId());

        Invoice invoice = invoiceRepository.findById(new InvoiceId(UUID.fromString(request.invoiceId())))
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with ID: " + request.invoiceId()));

        // Business rule: Ensure invoice amount aligns with claim logic if needed
        // Example: Claimed amount cannot exceed invoice amount by a certain factor.
        if (request.claimedAmount().compareTo(invoice.getAmount().multiply(java.math.BigDecimal.valueOf(1.5))) > 0) {
            throw new IllegalArgumentException("Claimed amount exceeds permissible limit relative to invoice amount.");
        }

        InsuranceClaim insuranceClaim = insuranceClaimMapper.toEntity(request);
        insuranceClaim.setInvoice(invoice); // Set the bidirectional relationship if modeled

        InsuranceClaim savedClaim = insuranceClaimRepository.save(insuranceClaim);
        log.info("Insurance claim created with ID: {}", savedClaim.getId().value());

        return insuranceClaimMapper.toResponse(savedClaim);
    }

    public InsuranceClaimResponse getInsuranceClaimById(String claimId) {
        log.info("Fetching insurance claim with ID: {}", claimId);
        InsuranceClaimId id = new InsuranceClaimId(UUID.fromString(claimId));
        InsuranceClaim claim = insuranceClaimRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InsuranceClaim not found with ID: " + claimId));
        return insuranceClaimMapper.toResponse(claim);
    }

    public List<InsuranceClaimResponse> getAllInsuranceClaims() {
        log.info("Fetching all insurance claims");
        return insuranceClaimRepository.findAll().stream()
                .map(insuranceClaimMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InsuranceClaimResponse updateInsuranceClaim(String claimId, InsuranceClaimRequest request) {
        log.info("Updating insurance claim with ID: {}", claimId);
        InsuranceClaimId id = new InsuranceClaimId(UUID.fromString(claimId));
        InsuranceClaim existingClaim = insuranceClaimRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InsuranceClaim not found with ID: " + claimId));

        // Update fields - using mapper for consistency
        insuranceClaimMapper.updateEntityFromRequest(request, existingClaim);

        // Re-fetch and update invoice if invoiceId changed
        if (request.invoiceId() != null && !existingClaim.getInvoice().getId().value().toString().equals(request.invoiceId())) {
            Invoice newInvoice = invoiceRepository.findById(new InvoiceId(UUID.fromString(request.invoiceId())))
                    .orElseThrow(() -> new EntityNotFoundException("New Invoice not found with ID: " + request.invoiceId()));
            existingClaim.setInvoice(newInvoice);
        }

        InsuranceClaim updatedClaim = insuranceClaimRepository.save(existingClaim);
        log.info("Insurance claim updated with ID: {}", updatedClaim.getId().value());
        return insuranceClaimMapper.toResponse(updatedClaim);
    }

    @Transactional
    public void deleteInsuranceClaim(String claimId) {
        log.info("Deleting insurance claim with ID: {}", claimId);
        InsuranceClaimId id = new InsuranceClaimId(UUID.fromString(claimId));
        if (!insuranceClaimRepository.existsById(id)) {
            throw new EntityNotFoundException("InsuranceClaim not found with ID: " + claimId);
        }
        insuranceClaimRepository.deleteById(id);
        log.info("Insurance claim deleted with ID: {}", claimId);
    }

    public List<InsuranceClaimResponse> getClaimsByInvoiceId(String invoiceId) {
        log.info("Fetching insurance claims for invoice ID: {}", invoiceId);
        InvoiceId id = new InvoiceId(UUID.fromString(invoiceId));
        // Assuming repository has a method to find by invoice. If not, implement a custom query.
        return insuranceClaimRepository.findByInvoiceId(id).stream()
                .map(insuranceClaimMapper::toResponse)
                .collect(Collectors.toList());
    }
}