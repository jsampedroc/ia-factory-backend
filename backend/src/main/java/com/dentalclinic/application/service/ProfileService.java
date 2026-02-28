package com.dentalclinic.application.service;

import com.dentalclinic.domain.model.Profile;
import com.dentalclinic.domain.valueobject.ProfileId;
import com.dentalclinic.domain.model.MedicalAlert;
import com.dentalclinic.domain.model.Allergy;
import com.dentalclinic.domain.model.Consent;
import com.dentalclinic.domain.valueobject.MedicalAlertId;
import com.dentalclinic.domain.valueobject.AllergyId;
import com.dentalclinic.domain.valueobject.ConsentId;
import com.dentalclinic.application.dto.MedicalAlertRequest;
import com.dentalclinic.application.dto.AllergyRequest;
import com.dentalclinic.application.dto.ConsentRequest;
import com.dentalclinic.application.dto.ProfileResponse;
import com.dentalclinic.domain.repository.ProfileRepository;
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
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public ProfileResponse getProfileById(UUID profileId) {
        Profile profile = profileRepository.findById(ProfileId.of(profileId))
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));
        return mapToProfileResponse(profile);
    }

    @Transactional
    public ProfileResponse addMedicalAlertToProfile(UUID profileId, MedicalAlertRequest request) {
        Profile profile = profileRepository.findById(ProfileId.of(profileId))
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        MedicalAlert newAlert = MedicalAlert.builder()
                .id(MedicalAlertId.of(UUID.randomUUID()))
                .description(request.description())
                .dateIssued(request.dateIssued())
                .build();

        profile.getMedicalAlerts().add(newAlert);
        Profile savedProfile = profileRepository.save(profile);
        log.info("MedicalAlert added to Profile with id: {}", profileId);
        return mapToProfileResponse(savedProfile);
    }

    @Transactional
    public ProfileResponse addAllergyToProfile(UUID profileId, AllergyRequest request) {
        Profile profile = profileRepository.findById(ProfileId.of(profileId))
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        Allergy newAllergy = Allergy.builder()
                .id(AllergyId.of(UUID.randomUUID()))
                .allergen(request.allergen())
                .reaction(request.reaction())
                .build();

        profile.getAllergies().add(newAllergy);
        Profile savedProfile = profileRepository.save(profile);
        log.info("Allergy added to Profile with id: {}", profileId);
        return mapToProfileResponse(savedProfile);
    }

    @Transactional
    public ProfileResponse addConsentToProfile(UUID profileId, ConsentRequest request) {
        Profile profile = profileRepository.findById(ProfileId.of(profileId))
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        Consent newConsent = Consent.builder()
                .id(ConsentId.of(UUID.randomUUID()))
                .description(request.description())
                .dateGiven(request.dateGiven())
                .build();

        profile.getConsents().add(newConsent);
        Profile savedProfile = profileRepository.save(profile);
        log.info("Consent added to Profile with id: {}", profileId);
        return mapToProfileResponse(savedProfile);
    }

    @Transactional
    public void removeMedicalAlertFromProfile(UUID profileId, UUID medicalAlertId) {
        Profile profile = profileRepository.findById(ProfileId.of(profileId))
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        boolean removed = profile.getMedicalAlerts().removeIf(alert -> alert.getId().value().equals(medicalAlertId));
        if (removed) {
            profileRepository.save(profile);
            log.info("MedicalAlert with id: {} removed from Profile with id: {}", medicalAlertId, profileId);
        } else {
            log.warn("MedicalAlert with id: {} not found in Profile with id: {}", medicalAlertId, profileId);
        }
    }

    @Transactional
    public void removeAllergyFromProfile(UUID profileId, UUID allergyId) {
        Profile profile = profileRepository.findById(ProfileId.of(profileId))
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        boolean removed = profile.getAllergies().removeIf(allergy -> allergy.getId().value().equals(allergyId));
        if (removed) {
            profileRepository.save(profile);
            log.info("Allergy with id: {} removed from Profile with id: {}", allergyId, profileId);
        } else {
            log.warn("Allergy with id: {} not found in Profile with id: {}", allergyId, profileId);
        }
    }

    @Transactional
    public void removeConsentFromProfile(UUID profileId, UUID consentId) {
        Profile profile = profileRepository.findById(ProfileId.of(profileId))
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        boolean removed = profile.getConsents().removeIf(consent -> consent.getId().value().equals(consentId));
        if (removed) {
            profileRepository.save(profile);
            log.info("Consent with id: {} removed from Profile with id: {}", consentId, profileId);
        } else {
            log.warn("Consent with id: {} not found in Profile with id: {}", consentId, profileId);
        }
    }

    private ProfileResponse mapToProfileResponse(Profile profile) {
        List<ProfileResponse.MedicalAlertResponse> medicalAlerts = profile.getMedicalAlerts().stream()
                .map(alert -> new ProfileResponse.MedicalAlertResponse(
                        alert.getId().value(),
                        alert.getDescription(),
                        alert.getDateIssued()
                )).collect(Collectors.toList());

        List<ProfileResponse.AllergyResponse> allergies = profile.getAllergies().stream()
                .map(allergy -> new ProfileResponse.AllergyResponse(
                        allergy.getId().value(),
                        allergy.getAllergen(),
                        allergy.getReaction()
                )).collect(Collectors.toList());

        List<ProfileResponse.ConsentResponse> consents = profile.getConsents().stream()
                .map(consent -> new ProfileResponse.ConsentResponse(
                        consent.getId().value(),
                        consent.getDescription(),
                        consent.getDateGiven()
                )).collect(Collectors.toList());

        return new ProfileResponse(
                profile.getId().value(),
                medicalAlerts,
                allergies,
                consents
        );
    }
}