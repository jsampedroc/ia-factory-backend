package com.dentalclinic.infrastructure.rest;

import com.dentalclinic.application.service.ProfileService;
import com.dentalclinic.infrastructure.rest.dto.ProfileRequest;
import com.dentalclinic.infrastructure.rest.dto.ProfileResponse;
import com.dentalclinic.domain.valueobject.ProfileId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse createdProfile = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable UUID id) {
        ProfileId profileId = new ProfileId(id);
        ProfileResponse profile = profileService.getProfileById(profileId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        List<ProfileResponse> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable UUID id,
                                                         @Valid @RequestBody ProfileRequest request) {
        ProfileId profileId = new ProfileId(id);
        ProfileResponse updatedProfile = profileService.updateProfile(profileId, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        ProfileId profileId = new ProfileId(id);
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{profileId}/medical-alerts")
    public ResponseEntity<ProfileResponse> addMedicalAlert(@PathVariable UUID profileId,
                                                           @Valid @RequestBody MedicalAlertRequest request) {
        ProfileId id = new ProfileId(profileId);
        ProfileResponse updatedProfile = profileService.addMedicalAlert(id, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/{profileId}/allergies")
    public ResponseEntity<ProfileResponse> addAllergy(@PathVariable UUID profileId,
                                                      @Valid @RequestBody AllergyRequest request) {
        ProfileId id = new ProfileId(profileId);
        ProfileResponse updatedProfile = profileService.addAllergy(id, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/{profileId}/consents")
    public ResponseEntity<ProfileResponse> addConsent(@PathVariable UUID profileId,
                                                      @Valid @RequestBody ConsentRequest request) {
        ProfileId id = new ProfileId(profileId);
        ProfileResponse updatedProfile = profileService.addConsent(id, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{profileId}/medical-alerts/{alertId}")
    public ResponseEntity<ProfileResponse> removeMedicalAlert(@PathVariable UUID profileId,
                                                              @PathVariable UUID alertId) {
        ProfileId pid = new ProfileId(profileId);
        ProfileResponse updatedProfile = profileService.removeMedicalAlert(pid, alertId);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{profileId}/allergies/{allergyId}")
    public ResponseEntity<ProfileResponse> removeAllergy(@PathVariable UUID profileId,
                                                         @PathVariable UUID allergyId) {
        ProfileId pid = new ProfileId(profileId);
        ProfileResponse updatedProfile = profileService.removeAllergy(pid, allergyId);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{profileId}/consents/{consentId}")
    public ResponseEntity<ProfileResponse> removeConsent(@PathVariable UUID profileId,
                                                         @PathVariable UUID consentId) {
        ProfileId pid = new ProfileId(profileId);
        ProfileResponse updatedProfile = profileService.removeConsent(pid, consentId);
        return ResponseEntity.ok(updatedProfile);
    }
}