package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.AllergyRequest;
import com.dentalclinic.application.dto.AllergyResponse;
import com.dentalclinic.application.mapper.AllergyMapper;
import com.dentalclinic.domain.model.Allergy;
import com.dentalclinic.domain.model.Profile;
import com.dentalclinic.domain.valueobject.AllergyId;
import com.dentalclinic.domain.valueobject.ProfileId;
import com.dentalclinic.port.in.AllergyUseCase;
import com.dentalclinic.port.out.AllergyRepositoryPort;
import com.dentalclinic.port.out.ProfileRepositoryPort;
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
public class AllergyService implements AllergyUseCase {

    private final AllergyRepositoryPort allergyRepositoryPort;
    private final ProfileRepositoryPort profileRepositoryPort;
    private final AllergyMapper allergyMapper;

    @Override
    @Transactional
    public AllergyResponse createAllergy(UUID profileId, AllergyRequest allergyRequest) {
        log.info("Creating allergy for profile ID: {}", profileId);

        Profile profile = profileRepositoryPort.findById(new ProfileId(profileId))
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + profileId));

        Allergy allergy = allergyMapper.toEntity(allergyRequest);
        profile.addAllergy(allergy);

        Allergy savedAllergy = allergyRepositoryPort.save(allergy);
        log.info("Allergy created with ID: {}", savedAllergy.getId().value());

        return allergyMapper.toResponse(savedAllergy);
    }

    @Override
    public AllergyResponse getAllergyById(UUID allergyId) {
        log.info("Fetching allergy with ID: {}", allergyId);
        Allergy allergy = allergyRepositoryPort.findById(new AllergyId(allergyId))
                .orElseThrow(() -> new IllegalArgumentException("Allergy not found with ID: " + allergyId));
        return allergyMapper.toResponse(allergy);
    }

    @Override
    public List<AllergyResponse> getAllergiesByProfileId(UUID profileId) {
        log.info("Fetching all allergies for profile ID: {}", profileId);
        Profile profile = profileRepositoryPort.findById(new ProfileId(profileId))
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + profileId));

        return profile.getAllergies().stream()
                .map(allergyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AllergyResponse updateAllergy(UUID allergyId, AllergyRequest allergyRequest) {
        log.info("Updating allergy with ID: {}", allergyId);
        Allergy existingAllergy = allergyRepositoryPort.findById(new AllergyId(allergyId))
                .orElseThrow(() -> new IllegalArgumentException("Allergy not found with ID: " + allergyId));

        existingAllergy.update(allergyRequest.getAllergen(), allergyRequest.getReaction());
        Allergy updatedAllergy = allergyRepositoryPort.save(existingAllergy);
        log.info("Allergy updated with ID: {}", updatedAllergy.getId().value());

        return allergyMapper.toResponse(updatedAllergy);
    }

    @Override
    @Transactional
    public void deleteAllergy(UUID allergyId) {
        log.info("Deleting allergy with ID: {}", allergyId);
        Allergy allergy = allergyRepositoryPort.findById(new AllergyId(allergyId))
                .orElseThrow(() -> new IllegalArgumentException("Allergy not found with ID: " + allergyId));

        allergyRepositoryPort.delete(allergy);
        log.info("Allergy deleted with ID: {}", allergyId);
    }
}