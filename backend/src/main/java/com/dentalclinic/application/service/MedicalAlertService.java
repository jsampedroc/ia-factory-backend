package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.MedicalAlertRequest;
import com.dentalclinic.application.dto.MedicalAlertResponse;
import com.dentalclinic.domain.model.MedicalAlert;
import com.dentalclinic.domain.model.Profile;
import com.dentalclinic.domain.valueobject.MedicalAlertId;
import com.dentalclinic.domain.valueobject.ProfileId;
import com.dentalclinic.domain.repository.MedicalAlertRepository;
import com.dentalclinic.domain.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalAlertService {

    private final MedicalAlertRepository medicalAlertRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public MedicalAlertResponse createMedicalAlert(MedicalAlertRequest request) {
        log.info("Creating medical alert for profile ID: {}", request.profileId());

        ProfileId profileId = new ProfileId(UUID.fromString(request.profileId()));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + request.profileId()));

        MedicalAlertId newId = new MedicalAlertId(UUID.randomUUID());
        MedicalAlert medicalAlert = MedicalAlert.builder()
                .id(newId)
                .description(request.description())
                .dateIssued(request.dateIssued() != null ? request.dateIssued() : LocalDate.now())
                .build();

        profile.addMedicalAlert(medicalAlert);
        profileRepository.save(profile);

        MedicalAlert savedAlert = medicalAlertRepository.save(medicalAlert);
        log.info("Medical alert created with ID: {}", savedAlert.getId().value());

        return mapToResponse(savedAlert);
    }

    public MedicalAlertResponse getMedicalAlertById(String alertId) {
        log.info("Fetching medical alert with ID: {}", alertId);
        MedicalAlertId id = new MedicalAlertId(UUID.fromString(alertId));
        MedicalAlert medicalAlert = medicalAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalAlert not found with ID: " + alertId));
        return mapToResponse(medicalAlert);
    }

    public List<MedicalAlertResponse> getMedicalAlertsByProfileId(String profileId) {
        log.info("Fetching medical alerts for profile ID: {}", profileId);
        ProfileId id = new ProfileId(UUID.fromString(profileId));
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + profileId));

        return profile.getMedicalAlerts().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalAlertResponse updateMedicalAlert(String alertId, MedicalAlertRequest request) {
        log.info("Updating medical alert with ID: {}", alertId);
        MedicalAlertId id = new MedicalAlertId(UUID.fromString(alertId));
        MedicalAlert medicalAlert = medicalAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalAlert not found with ID: " + alertId));

        medicalAlert.setDescription(request.description());
        if (request.dateIssued() != null) {
            medicalAlert.setDateIssued(request.dateIssued());
        }

        MedicalAlert updatedAlert = medicalAlertRepository.save(medicalAlert);
        log.info("Medical alert updated with ID: {}", updatedAlert.getId().value());
        return mapToResponse(updatedAlert);
    }

    @Transactional
    public void deleteMedicalAlert(String alertId) {
        log.info("Deleting medical alert with ID: {}", alertId);
        MedicalAlertId id = new MedicalAlertId(UUID.fromString(alertId));
        MedicalAlert medicalAlert = medicalAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalAlert not found with ID: " + alertId));

        Profile profile = profileRepository.findByMedicalAlertId(id)
                .orElseThrow(() -> new IllegalStateException("MedicalAlert is not associated with any profile."));

        profile.removeMedicalAlert(medicalAlert);
        profileRepository.save(profile);
        medicalAlertRepository.deleteById(id);
        log.info("Medical alert deleted with ID: {}", alertId);
    }

    private MedicalAlertResponse mapToResponse(MedicalAlert medicalAlert) {
        return new MedicalAlertResponse(
                medicalAlert.getId().value().toString(),
                medicalAlert.getDescription(),
                medicalAlert.getDateIssued()
        );
    }
}