package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.ClinicalNoteRequest;
import com.dentalclinic.application.dto.ClinicalNoteResponse;
import com.dentalclinic.application.mapper.ClinicalNoteMapper;
import com.dentalclinic.domain.model.ClinicalNote;
import com.dentalclinic.domain.model.ElectronicHealthRecord;
import com.dentalclinic.domain.model.Dentist;
import com.dentalclinic.domain.valueobject.ClinicalNoteId;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import com.dentalclinic.domain.valueobject.DentistId;
import com.dentalclinic.domain.repository.ClinicalNoteRepository;
import com.dentalclinic.domain.repository.ElectronicHealthRecordRepository;
import com.dentalclinic.domain.repository.DentistRepository;
import com.dentalclinic.domain.shared.AppointmentStatus;
import com.dentalclinic.domain.shared.RoleType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicalNoteService {

    private final ClinicalNoteRepository clinicalNoteRepository;
    private final ElectronicHealthRecordRepository ehrRepository;
    private final DentistRepository dentistRepository;
    private final ClinicalNoteMapper clinicalNoteMapper;

    @Transactional
    public ClinicalNoteResponse createClinicalNote(ClinicalNoteRequest request, UUID dentistUserId) {
        log.info("Creating clinical note for EHR ID: {} by dentist user ID: {}", request.ehrId(), dentistUserId);

        // 1. Fetch and validate EHR
        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(request.ehrId()))
                .orElseThrow(() -> new IllegalArgumentException("Electronic Health Record not found with ID: " + request.ehrId()));

        // 2. Fetch and validate Dentist entity (linked to the user performing the action)
        Dentist dentist = dentistRepository.findByUserId(dentistUserId)
                .orElseThrow(() -> new IllegalArgumentException("Dentist not found for user ID: " + dentistUserId));

        // 3. Business Rule: Only Dentists can add ClinicalNotes (Role-based access control)
        // This is enforced at the service layer. The dentist entity is fetched via user ID.
        // Additional check: Ensure the dentist is active/authorized if needed.
        log.debug("Dentist '{}' authorized to create clinical note.", dentist.getName());

        // 4. Create the ClinicalNote entity
        ClinicalNote newNote = ClinicalNote.builder()
                .id(new ClinicalNoteId(UUID.randomUUID()))
                .note(request.note())
                .timestamp(LocalDateTime.now())
                .build();

        // 5. Add note to the EHR's list (maintaining the 1:N relationship)
        ehr.getClinicalNotes().add(newNote);

        // 6. Save the EHR (cascade should persist the new ClinicalNote if mapped correctly)
        ehrRepository.save(ehr);
        log.info("Clinical note created with ID: {}", newNote.getId().value());

        return clinicalNoteMapper.toResponse(newNote);
    }

    public ClinicalNoteResponse getClinicalNoteById(UUID noteId) {
        log.info("Fetching clinical note with ID: {}", noteId);
        ClinicalNote note = clinicalNoteRepository.findById(new ClinicalNoteId(noteId))
                .orElseThrow(() -> new IllegalArgumentException("Clinical Note not found with ID: " + noteId));
        return clinicalNoteMapper.toResponse(note);
    }

    public List<ClinicalNoteResponse> getClinicalNotesByEhrId(UUID ehrId) {
        log.info("Fetching all clinical notes for EHR ID: {}", ehrId);
        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(ehrId))
                .orElseThrow(() -> new IllegalArgumentException("Electronic Health Record not found with ID: " + ehrId));

        return ehr.getClinicalNotes().stream()
                .map(clinicalNoteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClinicalNoteResponse updateClinicalNote(UUID noteId, ClinicalNoteRequest request, UUID dentistUserId) {
        log.info("Updating clinical note with ID: {} by dentist user ID: {}", noteId, dentistUserId);

        // 1. Fetch and validate the existing note
        ClinicalNote existingNote = clinicalNoteRepository.findById(new ClinicalNoteId(noteId))
                .orElseThrow(() -> new IllegalArgumentException("Clinical Note not found with ID: " + noteId));

        // 2. Fetch and validate Dentist (authorization check)
        Dentist dentist = dentistRepository.findByUserId(dentistUserId)
                .orElseThrow(() -> new IllegalArgumentException("Dentist not found for user ID: " + dentistUserId));
        log.debug("Dentist '{}' authorized to update clinical note.", dentist.getName());

        // 3. Update the note content
        existingNote.setNote(request.note());
        // Timestamp could be updated to 'lastModified' if needed, but original timestamp should likely remain.

        ClinicalNote updatedNote = clinicalNoteRepository.save(existingNote);
        log.info("Clinical note updated with ID: {}", updatedNote.getId().value());

        return clinicalNoteMapper.toResponse(updatedNote);
    }

    @Transactional
    public void deleteClinicalNote(UUID noteId, UUID dentistUserId) {
        log.info("Deleting clinical note with ID: {} by dentist user ID: {}", noteId, dentistUserId);

        // 1. Fetch and validate the note
        ClinicalNote noteToDelete = clinicalNoteRepository.findById(new ClinicalNoteId(noteId))
                .orElseThrow(() -> new IllegalArgumentException("Clinical Note not found with ID: " + noteId));

        // 2. Fetch and validate Dentist (authorization check)
        Dentist dentist = dentistRepository.findByUserId(dentistUserId)
                .orElseThrow(() -> new IllegalArgumentException("Dentist not found for user ID: " + dentistUserId));
        log.debug("Dentist '{}' authorized to delete clinical note.", dentist.getName());

        // 3. Find the associated EHR to remove the note from its collection (maintain referential integrity)
        ElectronicHealthRecord associatedEhr = ehrRepository.findByClinicalNoteId(new ClinicalNoteId(noteId))
                .orElseThrow(() -> new IllegalStateException("Could not find parent EHR for clinical note ID: " + noteId));

        associatedEhr.getClinicalNotes().removeIf(n -> n.getId().equals(noteToDelete.getId()));
        ehrRepository.save(associatedEhr);

        // 4. Delete the note entity
        clinicalNoteRepository.delete(noteToDelete);
        log.info("Clinical note deleted with ID: {}", noteId);
    }
}