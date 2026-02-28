package com.dentalclinic.application.service;

import com.dentalclinic.domain.model.TreatmentAuditLog;
import com.dentalclinic.domain.model.ElectronicHealthRecord;
import com.dentalclinic.domain.model.Patient;
import com.dentalclinic.domain.valueobject.TreatmentAuditLogId;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import com.dentalclinic.domain.valueobject.PatientId;
import com.dentalclinic.domain.repository.TreatmentAuditLogRepository;
import com.dentalclinic.domain.repository.ElectronicHealthRecordRepository;
import com.dentalclinic.domain.repository.PatientRepository;
import com.dentalclinic.application.dto.TreatmentAuditLogRequest;
import com.dentalclinic.application.dto.TreatmentAuditLogResponse;
import com.dentalclinic.application.exception.ResourceNotFoundException;
import com.dentalclinic.application.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TreatmentAuditLogService {

    private final TreatmentAuditLogRepository treatmentAuditLogRepository;
    private final ElectronicHealthRecordRepository ehrRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public TreatmentAuditLogResponse createTreatmentAuditLog(TreatmentAuditLogRequest request) {
        log.info("Creating TreatmentAuditLog for EHR ID: {}", request.ehrId());

        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(request.ehrId()))
                .orElseThrow(() -> new ResourceNotFoundException("ElectronicHealthRecord not found with ID: " + request.ehrId()));

        Patient patient = patientRepository.findByHealthRecordsContaining(ehr)
                .orElseThrow(() -> new BusinessRuleViolationException("Patient not found for the given EHR."));

        validateConsentForTreatment(patient, request.timestamp());

        TreatmentAuditLog newLog = TreatmentAuditLog.builder()
                .id(new TreatmentAuditLogId(UUID.randomUUID()))
                .treatmentDetails(request.treatmentDetails())
                .timestamp(request.timestamp() != null ? request.timestamp() : LocalDateTime.now())
                .build();

        ehr.addTreatmentAuditLog(newLog);
        ehrRepository.save(ehr);

        log.info("TreatmentAuditLog created with ID: {}", newLog.getId().value());
        return mapToResponse(newLog);
    }

    public TreatmentAuditLogResponse getTreatmentAuditLogById(UUID id) {
        log.debug("Fetching TreatmentAuditLog with ID: {}", id);
        TreatmentAuditLog logEntity = treatmentAuditLogRepository.findById(new TreatmentAuditLogId(id))
                .orElseThrow(() -> new ResourceNotFoundException("TreatmentAuditLog not found with ID: " + id));
        return mapToResponse(logEntity);
    }

    public List<TreatmentAuditLogResponse> getTreatmentAuditLogsByEhrId(UUID ehrId) {
        log.debug("Fetching TreatmentAuditLogs for EHR ID: {}", ehrId);
        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(ehrId))
                .orElseThrow(() -> new ResourceNotFoundException("ElectronicHealthRecord not found with ID: " + ehrId));

        return ehr.getTreatmentAuditLogs().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TreatmentAuditLogResponse> getTreatmentAuditLogsByPatientId(UUID patientId) {
        log.debug("Fetching TreatmentAuditLogs for Patient ID: {}", patientId);
        Patient patient = patientRepository.findById(new PatientId(patientId))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        return patient.getHealthRecords().stream()
                .flatMap(ehr -> ehr.getTreatmentAuditLogs().stream())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTreatmentAuditLog(UUID id) {
        log.info("Deleting TreatmentAuditLog with ID: {}", id);
        TreatmentAuditLog logEntity = treatmentAuditLogRepository.findById(new TreatmentAuditLogId(id))
                .orElseThrow(() -> new ResourceNotFoundException("TreatmentAuditLog not found with ID: " + id));

        ElectronicHealthRecord ehr = ehrRepository.findByTreatmentAuditLogsContaining(logEntity)
                .orElseThrow(() -> new BusinessRuleViolationException("Associated EHR not found for TreatmentAuditLog."));

        ehr.removeTreatmentAuditLog(logEntity);
        ehrRepository.save(ehr);
        treatmentAuditLogRepository.delete(logEntity);
        log.info("TreatmentAuditLog deleted with ID: {}", id);
    }

    private void validateConsentForTreatment(Patient patient, LocalDateTime treatmentTime) {
        boolean hasValidConsent = patient.getProfile().getConsents().stream()
                .anyMatch(consent -> consent.getDateGiven().isBefore(treatmentTime.toLocalDate()));

        if (!hasValidConsent) {
            throw new BusinessRuleViolationException(
                    "Cannot log treatment. A valid Consent must be recorded before the treatment date."
            );
        }
    }

    private TreatmentAuditLogResponse mapToResponse(TreatmentAuditLog treatmentAuditLog) {
        return new TreatmentAuditLogResponse(
                treatmentAuditLog.getId().value(),
                treatmentAuditLog.getTreatmentDetails(),
                treatmentAuditLog.getTimestamp()
        );
    }
}