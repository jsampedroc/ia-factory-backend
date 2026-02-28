package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.ConsentService;
import com.dentalclinic.infrastructure.rest.dto.ConsentRequest;
import com.dentalclinic.infrastructure.rest.dto.ConsentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/consents")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping
    public ResponseEntity<ConsentResponse> createConsent(@Valid @RequestBody ConsentRequest request) {
        ConsentResponse createdConsent = consentService.createConsent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConsent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsentResponse> getConsentById(@PathVariable UUID id) {
        ConsentResponse consent = consentService.getConsentById(id);
        return ResponseEntity.ok(consent);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ConsentResponse>> getConsentsByProfileId(@PathVariable UUID profileId) {
        List<ConsentResponse> consents = consentService.getConsentsByProfileId(profileId);
        return ResponseEntity.ok(consents);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsentResponse> updateConsent(@PathVariable UUID id,
                                                         @Valid @RequestBody ConsentRequest request) {
        ConsentResponse updatedConsent = consentService.updateConsent(id, request);
        return ResponseEntity.ok(updatedConsent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsent(@PathVariable UUID id) {
        consentService.deleteConsent(id);
        return ResponseEntity.noContent().build();
    }
}