package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.PatientRequest;
import com.dentalclinic.application.dto.PatientResponse;
import com.dentalclinic.application.mapper.PatientMapper;
import com.dentalclinic.domain.model.Patient;
import com.dentalclinic.domain.valueobject.PatientId;
import com.dentalclinic.domain.repository.PatientRepository;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponse createPatient(PatientRequest patientRequest) {
        log.info("Creating new patient with name: {}", patientRequest.name());
        Patient patient = patientMapper.toEntity(patientRequest);
        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient created with ID: {}", savedPatient.getId().value());
        return patientMapper.toResponse(savedPatient);
    }

    public PatientResponse getPatientById(UUID id) {
        log.info("Fetching patient with ID: {}", id);
        PatientId patientId = new PatientId(id);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
        return patientMapper.toResponse(patient);
    }

    public List<PatientResponse> getAllPatients() {
        log.info("Fetching all patients");
        return patientRepository.findAll().stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponse updatePatient(UUID id, PatientRequest patientRequest) {
        log.info("Updating patient with ID: {}", id);
        PatientId patientId = new PatientId(id);
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));

        Patient updatedPatient = patientMapper.updateEntityFromRequest(patientRequest, existingPatient);
        Patient savedPatient = patientRepository.save(updatedPatient);
        log.info("Patient updated with ID: {}", savedPatient.getId().value());
        return patientMapper.toResponse(savedPatient);
    }

    @Transactional
    public void deletePatient(UUID id) {
        log.info("Deleting patient with ID: {}", id);
        PatientId patientId = new PatientId(id);
        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(patientId);
        log.info("Patient deleted with ID: {}", id);
    }
}