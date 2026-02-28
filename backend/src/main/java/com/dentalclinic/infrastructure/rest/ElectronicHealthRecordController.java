package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.ElectronicHealthRecordService;
import com.dentalclinic.infrastructure.rest.dto.ElectronicHealthRecordRequest;
import com.dentalclinic.infrastructure.rest.dto.ElectronicHealthRecordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/electronic-health-records")
@RequiredArgsConstructor
public class ElectronicHealthRecordController {

    private final ElectronicHealthRecordService electronicHealthRecordService;

    @PostMapping
    public ResponseEntity<ElectronicHealthRecordResponse> create(@Valid @RequestBody ElectronicHealthRecordRequest request) {
        ElectronicHealthRecordResponse created = electronicHealthRecordService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectronicHealthRecordResponse> findById(@PathVariable UUID id) {
        ElectronicHealthRecordResponse found = electronicHealthRecordService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ElectronicHealthRecordResponse>> findByPatientId(@PathVariable UUID patientId) {
        List<ElectronicHealthRecordResponse> records = electronicHealthRecordService.findByPatientId(patientId);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ElectronicHealthRecordResponse> update(@PathVariable UUID id,
                                                                 @Valid @RequestBody ElectronicHealthRecordRequest request) {
        ElectronicHealthRecordResponse updated = electronicHealthRecordService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        electronicHealthRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}