package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.dto.OdontogramRequest;
import com.dentalclinic.application.dto.OdontogramResponse;
import com.dentalclinic.application.service.OdontogramService;
import com.dentalclinic.domain.valueobject.OdontogramId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/odontograms")
@RequiredArgsConstructor
public class OdontogramController {

    private final OdontogramService odontogramService;

    @PostMapping
    public ResponseEntity<OdontogramResponse> create(@Valid @RequestBody OdontogramRequest request) {
        OdontogramResponse created = odontogramService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OdontogramResponse> getById(@PathVariable UUID id) {
        OdontogramId odontogramId = new OdontogramId(id);
        OdontogramResponse response = odontogramService.getById(odontogramId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ehr/{ehrId}")
    public ResponseEntity<List<OdontogramResponse>> getByEhrId(@PathVariable UUID ehrId) {
        List<OdontogramResponse> response = odontogramService.getByEhrId(ehrId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OdontogramResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody OdontogramRequest request) {
        OdontogramId odontogramId = new OdontogramId(id);
        OdontogramResponse updated = odontogramService.update(odontogramId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        OdontogramId odontogramId = new OdontogramId(id);
        odontogramService.delete(odontogramId);
        return ResponseEntity.noContent().build();
    }
}