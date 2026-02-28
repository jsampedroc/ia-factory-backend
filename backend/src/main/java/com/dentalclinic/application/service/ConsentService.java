package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.ConsentRequest;
import com.dentalclinic.application.dto.ConsentResponse;
import com.dentalclinic.application.mapper.ConsentMapper;
import com.dentalclinic.domain.model.Consent;
import com.dentalclinic.domain.model.Profile;
import com.dentalclinic.domain.valueobject.ConsentId;
import com.dentalclinic.domain.valueobject.ProfileId;
import com.dentalclinic.port.in.ConsentUseCase;
import com.dentalclinic.port.out.ConsentRepository;
import com.dentalclinic.port.out.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConsentService implements ConsentUseCase {

    private final ConsentRepository consentRepository;
    private final ProfileRepository profileRepository;
    private final ConsentMapper consentMapper;

    @Override
    public ConsentResponse createConsent(UUID profileId, ConsentRequest request) {
        log.info("Creating consent for profile ID: {}", profileId);

        Profile profile = profileRepository.findById(new ProfileId(profileId))
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + profileId));

        Consent consent = consentMapper.toEntity(request);
        profile.addConsent(consent);

        Consent savedConsent = consentRepository.save(consent);
        profileRepository.save(profile);

        log.info("Consent created with ID: {}", savedConsent.getId().value());
        return consentMapper.toResponse(savedConsent);
    }

    @Override
    public ConsentResponse getConsentById(UUID consentId) {
        log.info("Fetching consent with ID: {}", consentId);
        Consent consent = consentRepository.findById(new ConsentId(consentId))
                .orElseThrow(() -> new IllegalArgumentException("Consent not found with ID: " + consentId));
        return consentMapper.toResponse(consent);
    }

    @Override
    public List<ConsentResponse> getConsentsByProfileId(UUID profileId) {
        log.info("Fetching all consents for profile ID: {}", profileId);
        Profile profile = profileRepository.findById(new ProfileId(profileId))
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + profileId));

        return profile.getConsents().stream()
                .map(consentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConsentResponse updateConsent(UUID consentId, ConsentRequest request) {
        log.info("Updating consent with ID: {}", consentId);
        Consent existingConsent = consentRepository.findById(new ConsentId(consentId))
                .orElseThrow(() -> new IllegalArgumentException("Consent not found with ID: " + consentId));

        Consent updatedConsent = consentMapper.updateEntity(existingConsent, request);
        Consent savedConsent = consentRepository.save(updatedConsent);

        log.info("Consent updated with ID: {}", savedConsent.getId().value());
        return consentMapper.toResponse(savedConsent);
    }

    @Override
    public void deleteConsent(UUID consentId) {
        log.info("Deleting consent with ID: {}", consentId);
        Consent consent = consentRepository.findById(new ConsentId(consentId))
                .orElseThrow(() -> new IllegalArgumentException("Consent not found with ID: " + consentId));

        // Remove consent from its profile
        Profile profile = profileRepository.findByConsentId(new ConsentId(consentId))
                .orElseThrow(() -> new IllegalStateException("Associated profile not found for consent ID: " + consentId));
        profile.removeConsent(consent);

        consentRepository.deleteById(new ConsentId(consentId));
        profileRepository.save(profile);
        log.info("Consent deleted with ID: {}", consentId);
    }

    @Override
    public void validateConsentExistsForTreatment(UUID profileId) {
        log.debug("Validating consent exists for profile ID: {} before treatment", profileId);
        List<ConsentResponse> consents = getConsentsByProfileId(profileId);
        if (consents.isEmpty()) {
            throw new IllegalStateException("Cannot proceed with treatment. No consent recorded for profile ID: " + profileId);
        }
        log.debug("Consent validation passed for profile ID: {}", profileId);
    }
}