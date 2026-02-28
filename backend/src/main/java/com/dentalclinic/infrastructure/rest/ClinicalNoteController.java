package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.services.ClinicalNoteService;
import com.dentalclinic.infrastructure.rest.dto.ClinicalNoteRequest;
import com.dentalclinic.infrastructure.rest.dto.ClinicalNoteResponse;
import com.dentalclinic.domain.valueobject.ClinicalNoteId;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clinical-notes")
@RequiredArgsConstructor
public class ClinicalNoteController {

    private final ClinicalNoteService clinicalNoteService;

    @PostMapping("/ehr/{ehrId}")
    public ResponseEntity<ClinicalNoteResponse> createClinicalNote(
            @PathVariable UUID ehrId,
            @Valid @RequestBody ClinicalNoteRequest request) {
        ClinicalNoteResponse response = clinicalNoteService.createClinicalNote(
                new ElectronicHealthRecordId(ehrId), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalNoteResponse> getClinicalNoteById(@PathVariable UUID id) {
        ClinicalNoteResponse response = clinicalNoteService.getClinicalNoteById(new ClinicalNoteId(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ehr/{ehrId}")
    public ResponseEntity<List<ClinicalNoteResponse>> getAllClinicalNotesForEhr(@PathVariable UUID ehrId) {
        List<ClinicalNoteResponse> responses = clinicalNoteService.getAllClinicalNotesForEhr(new ElectronicHealthRecordId(ehrId));
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicalNoteResponse> updateClinicalNote(
            @PathVariable UUID id,
            @Valid @RequestBody ClinicalNoteRequest request) {
        ClinicalNoteResponse response = clinicalNoteService.updateClinicalNote(new ClinicalNoteId(id), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinicalNote(@PathVariable UUID id) {
        clinicalNoteService.deleteClinicalNote(new ClinicalNoteId(id));
        return ResponseEntity.noContent().build();
    }
}