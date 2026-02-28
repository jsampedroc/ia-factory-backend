package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.dto.DentistRequest;
import com.dentalclinic.application.dto.DentistResponse;
import com.dentalclinic.application.service.DentistService;
import com.dentalclinic.domain.valueobject.DentistId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;

    @PostMapping
    public ResponseEntity<DentistResponse> createDentist(@Valid @RequestBody DentistRequest request) {
        DentistResponse createdDentist = dentistService.createDentist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDentist);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponse> getDentistById(@PathVariable UUID id) {
        DentistId dentistId = new DentistId(id);
        DentistResponse dentist = dentistService.getDentistById(dentistId);
        return ResponseEntity.ok(dentist);
    }

    @GetMapping
    public ResponseEntity<List<DentistResponse>> getAllDentists() {
        List<DentistResponse> dentists = dentistService.getAllDentists();
        return ResponseEntity.ok(dentists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistResponse> updateDentist(@PathVariable UUID id,
                                                         @Valid @RequestBody DentistRequest request) {
        DentistId dentistId = new DentistId(id);
        DentistResponse updatedDentist = dentistService.updateDentist(dentistId, request);
        return ResponseEntity.ok(updatedDentist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDentist(@PathVariable UUID id) {
        DentistId dentistId = new DentistId(id);
        dentistService.deleteDentist(dentistId);
        return ResponseEntity.noContent().build();
    }
}