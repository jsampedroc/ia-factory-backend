package com.dentalclinic.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PatientRequest {

    @NotBlank(message = "Patient name is mandatory")
    private String name;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Profile ID is mandatory")
    private UUID profileId;

    private List<UUID> healthRecordIds;
    private List<UUID> appointmentIds;
    private List<UUID> invoiceIds;

    // Constructors
    public PatientRequest() {
    }

    public PatientRequest(String name, LocalDate dateOfBirth, UUID profileId, List<UUID> healthRecordIds, List<UUID> appointmentIds, List<UUID> invoiceIds) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.profileId = profileId;
        this.healthRecordIds = healthRecordIds;
        this.appointmentIds = appointmentIds;
        this.invoiceIds = invoiceIds;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public List<UUID> getHealthRecordIds() {
        return healthRecordIds;
    }

    public void setHealthRecordIds(List<UUID> healthRecordIds) {
        this.healthRecordIds = healthRecordIds;
    }

    public List<UUID> getAppointmentIds() {
        return appointmentIds;
    }

    public void setAppointmentIds(List<UUID> appointmentIds) {
        this.appointmentIds = appointmentIds;
    }

    public List<UUID> getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(List<UUID> invoiceIds) {
        this.invoiceIds = invoiceIds;
    }
}