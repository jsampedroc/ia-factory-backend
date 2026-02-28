package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.shared.AppointmentStatus;
import com.dentalclinic.domain.valueobject.AppointmentId;
import com.dentalclinic.domain.valueobject.DentistId;
import com.dentalclinic.domain.valueobject.PatientId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class Appointment extends Entity<AppointmentId> {

    @NotNull(message = "Scheduled time cannot be null")
    @FutureOrPresent(message = "Scheduled time must be in the present or future")
    private LocalDateTime scheduledTime;

    @NotNull(message = "Appointment status cannot be null")
    private AppointmentStatus status;

    @NotNull(message = "Dentist ID cannot be null")
    private DentistId dentistId;

    @NotNull(message = "Patient ID cannot be null")
    private PatientId patientId;

    public void confirm() {
        if (this.status == AppointmentStatus.SCHEDULED) {
            this.status = AppointmentStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("Appointment must be in SCHEDULED state to be confirmed.");
        }
    }

    public void complete() {
        if (this.status == AppointmentStatus.CONFIRMED) {
            this.status = AppointmentStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Appointment must be in CONFIRMED state to be completed.");
        }
    }

    public void cancel() {
        if (this.status != AppointmentStatus.CANCELLED && this.status != AppointmentStatus.COMPLETED) {
            this.status = AppointmentStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Cannot cancel an appointment that is already CANCELLED or COMPLETED.");
        }
    }
}