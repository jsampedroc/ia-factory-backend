package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.TreatmentAuditLogService;
import com.dentalclinic.infrastructure.rest.dto.TreatmentAuditLogRequest;
import com.dentalclinic.infrastructure.rest.dto.TreatmentAuditLogResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/treatment-audit-logs")
@RequiredArgsConstructor
public class TreatmentAuditLogController {

    private final TreatmentAuditLogService treatmentAuditLogService;

    @PostMapping
    public ResponseEntity<TreatmentAuditLogResponse> create(@Valid @RequestBody TreatmentAuditLogRequest request) {
        TreatmentAuditLogResponse createdLog = treatmentAuditLogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentAuditLogResponse> getById(@PathVariable UUID id) {
        TreatmentAuditLogResponse log = treatmentAuditLogService.findById(id);
        return ResponseEntity.ok(log);
    }

    @GetMapping("/ehr/{ehrId}")
    public ResponseEntity<List<TreatmentAuditLogResponse>> getAllByEhrId(@PathVariable UUID ehrId) {
        List<TreatmentAuditLogResponse> logs = treatmentAuditLogService.findAllByEhrId(ehrId);
        return ResponseEntity.ok(logs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentAuditLogResponse> update(@PathVariable UUID id,
                                                            @Valid @RequestBody TreatmentAuditLogRequest request) {
        TreatmentAuditLogResponse updatedLog = treatmentAuditLogService.update(id, request);
        return ResponseEntity.ok(updatedLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        treatmentAuditLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}