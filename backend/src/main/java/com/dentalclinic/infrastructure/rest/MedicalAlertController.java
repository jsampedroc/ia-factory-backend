package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.MedicalAlertService;
import com.dentalclinic.infrastructure.rest.dto.MedicalAlertRequest;
import com.dentalclinic.infrastructure.rest.dto.MedicalAlertResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medical-alerts")
@RequiredArgsConstructor
public class MedicalAlertController {

    private final MedicalAlertService medicalAlertService;

    @PostMapping
    public ResponseEntity<MedicalAlertResponse> createMedicalAlert(@Valid @RequestBody MedicalAlertRequest request) {
        MedicalAlertResponse createdAlert = medicalAlertService.createMedicalAlert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlert);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalAlertResponse> getMedicalAlertById(@PathVariable UUID id) {
        MedicalAlertResponse alert = medicalAlertService.getMedicalAlertById(id);
        return ResponseEntity.ok(alert);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<MedicalAlertResponse>> getMedicalAlertsByProfile(@PathVariable UUID profileId) {
        List<MedicalAlertResponse> alerts = medicalAlertService.getMedicalAlertsByProfileId(profileId);
        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalAlertResponse> updateMedicalAlert(
            @PathVariable UUID id,
            @Valid @RequestBody MedicalAlertRequest request) {
        MedicalAlertResponse updatedAlert = medicalAlertService.updateMedicalAlert(id, request);
        return ResponseEntity.ok(updatedAlert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalAlert(@PathVariable UUID id) {
        medicalAlertService.deleteMedicalAlert(id);
        return ResponseEntity.noContent().build();
    }
}