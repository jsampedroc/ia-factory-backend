package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.services.AllergyService;
import com.dentalclinic.infrastructure.rest.dto.AllergyRequest;
import com.dentalclinic.infrastructure.rest.dto.AllergyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/allergies")
@RequiredArgsConstructor
public class AllergyController {

    private final AllergyService allergyService;

    @PostMapping
    public ResponseEntity<AllergyResponse> createAllergy(
            @PathVariable UUID profileId,
            @Valid @RequestBody AllergyRequest request) {
        AllergyResponse createdAllergy = allergyService.createAllergy(profileId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAllergy);
    }

    @GetMapping("/{allergyId}")
    public ResponseEntity<AllergyResponse> getAllergyById(
            @PathVariable UUID profileId,
            @PathVariable UUID allergyId) {
        AllergyResponse allergy = allergyService.getAllergyById(profileId, allergyId);
        return ResponseEntity.ok(allergy);
    }

    @GetMapping
    public ResponseEntity<List<AllergyResponse>> getAllAllergiesForProfile(
            @PathVariable UUID profileId) {
        List<AllergyResponse> allergies = allergyService.getAllAllergiesForProfile(profileId);
        return ResponseEntity.ok(allergies);
    }

    @PutMapping("/{allergyId}")
    public ResponseEntity<AllergyResponse> updateAllergy(
            @PathVariable UUID profileId,
            @PathVariable UUID allergyId,
            @Valid @RequestBody AllergyRequest request) {
        AllergyResponse updatedAllergy = allergyService.updateAllergy(profileId, allergyId, request);
        return ResponseEntity.ok(updatedAllergy);
    }

    @DeleteMapping("/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(
            @PathVariable UUID profileId,
            @PathVariable UUID allergyId) {
        allergyService.deleteAllergy(profileId, allergyId);
        return ResponseEntity.noContent().build();
    }
}