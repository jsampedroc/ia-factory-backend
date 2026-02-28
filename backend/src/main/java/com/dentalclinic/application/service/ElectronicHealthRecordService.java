package com.dentalclinic.application.service;

import com.dentalclinic.domain.model.ElectronicHealthRecord;
import com.dentalclinic.domain.model.ClinicalNote;
import com.dentalclinic.domain.model.Odontogram;
import com.dentalclinic.domain.model.TreatmentAuditLog;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import com.dentalclinic.domain.valueobject.PatientId;
import com.dentalclinic.domain.repository.ElectronicHealthRecordRepository;
import com.dentalclinic.domain.repository.PatientRepository;
import com.dentalclinic.application.dto.ClinicalNoteRequest;
import com.dentalclinic.application.dto.OdontogramRequest;
import com.dentalclinic.application.dto.TreatmentAuditLogRequest;
import com.dentalclinic.application.dto.ElectronicHealthRecordResponse;
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
public class ElectronicHealthRecordService {

    private final ElectronicHealthRecordRepository ehrRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public ElectronicHealthRecordResponse findById(UUID id) {
        log.debug("Finding EHR with ID: {}", id);
        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(id))
                .orElseThrow(() -> new RuntimeException("Electronic Health Record not found with id: " + id));
        return mapToResponse(ehr);
    }

    @Transactional(readOnly = true)
    public List<ElectronicHealthRecordResponse> findByPatientId(UUID patientId) {
        log.debug("Finding all EHRs for Patient ID: {}", patientId);
        List<ElectronicHealthRecord> ehrs = ehrRepository.findByPatientId(new PatientId(patientId));
        return ehrs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ElectronicHealthRecordResponse createForPatient(UUID patientId) {
        log.debug("Creating new EHR for Patient ID: {}", patientId);
        var patient = patientRepository.findById(new PatientId(patientId))
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        ElectronicHealthRecord newEhr = ElectronicHealthRecord.builder()
                .id(new ElectronicHealthRecordId(UUID.randomUUID()))
                .patient(patient)
                .clinicalNotes(List.of())
                .treatmentAuditLogs(List.of())
                .build();

        ElectronicHealthRecord savedEhr = ehrRepository.save(newEhr);
        log.info("Created new EHR with ID: {} for Patient ID: {}", savedEhr.getId().value(), patientId);
        return mapToResponse(savedEhr);
    }

    @Transactional
    public ElectronicHealthRecordResponse addClinicalNote(UUID ehrId, ClinicalNoteRequest request) {
        log.debug("Adding Clinical Note to EHR ID: {}", ehrId);
        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(ehrId))
                .orElseThrow(() -> new RuntimeException("Electronic Health Record not found with id: " + ehrId));

        ClinicalNote newNote = ClinicalNote.builder()
                .id(UUID.randomUUID())
                .note(request.note())
                .timestamp(LocalDateTime.now())
                .build();

        ehr.getClinicalNotes().add(newNote);
        ElectronicHealthRecord updatedEhr = ehrRepository.save(ehr);
        log.info("Added Clinical Note to EHR ID: {}", ehrId);
        return mapToResponse(updatedEhr);
    }

    @Transactional
    public ElectronicHealthRecordResponse updateOdontogram(UUID ehrId, OdontogramRequest request) {
        log.debug("Updating Odontogram for EHR ID: {}", ehrId);
        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(ehrId))
                .orElseThrow(() -> new RuntimeException("Electronic Health Record not found with id: " + ehrId));

        Odontogram odontogram = Odontogram.builder()
                .id(UUID.randomUUID())
                .diagram(request.diagram())
                .build();

        ehr.setOdontogram(odontogram);
        ElectronicHealthRecord updatedEhr = ehrRepository.save(ehr);
        log.info("Updated Odontogram for EHR ID: {}", ehrId);
        return mapToResponse(updatedEhr);
    }

    @Transactional
    public ElectronicHealthRecordResponse addTreatmentAuditLog(UUID ehrId, TreatmentAuditLogRequest request) {
        log.debug("Adding Treatment Audit Log to EHR ID: {}", ehrId);
        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(ehrId))
                .orElseThrow(() -> new RuntimeException("Electronic Health Record not found with id: " + ehrId));

        TreatmentAuditLog newLog = TreatmentAuditLog.builder()
                .id(UUID.randomUUID())
                .treatmentDetails(request.treatmentDetails())
                .timestamp(LocalDateTime.now())
                .build();

        ehr.getTreatmentAuditLogs().add(newLog);
        ElectronicHealthRecord updatedEhr = ehrRepository.save(ehr);
        log.info("Added Treatment Audit Log to EHR ID: {}", ehrId);
        return mapToResponse(updatedEhr);
    }

    @Transactional
    public void delete(UUID id) {
        log.debug("Deleting EHR with ID: {}", id);
        ElectronicHealthRecordId ehrId = new ElectronicHealthRecordId(id);
        if (!ehrRepository.existsById(ehrId)) {
            throw new RuntimeException("Electronic Health Record not found with id: " + id);
        }
        ehrRepository.deleteById(ehrId);
        log.info("Deleted EHR with ID: {}", id);
    }

    private ElectronicHealthRecordResponse mapToResponse(ElectronicHealthRecord ehr) {
        return new ElectronicHealthRecordResponse(
                ehr.getId().value(),
                ehr.getPatient().getId().value(),
                ehr.getClinicalNotes(),
                ehr.getOdontogram(),
                ehr.getTreatmentAuditLogs()
        );
    }
}