package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.AppointmentRequest;
import com.dentalclinic.application.dto.AppointmentResponse;
import com.dentalclinic.application.port.in.AppointmentUseCase;
import com.dentalclinic.application.port.out.AppointmentRepositoryPort;
import com.dentalclinic.application.port.out.DentistRepositoryPort;
import com.dentalclinic.application.port.out.PatientRepositoryPort;
import com.dentalclinic.domain.model.Appointment;
import com.dentalclinic.domain.model.Dentist;
import com.dentalclinic.domain.model.Patient;
import com.dentalclinic.domain.shared.AppointmentStatus;
import com.dentalclinic.domain.valueobject.AppointmentId;
import com.dentalclinic.domain.valueobject.DentistId;
import com.dentalclinic.domain.valueobject.PatientId;
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
public class AppointmentService implements AppointmentUseCase {

    private final AppointmentRepositoryPort appointmentRepositoryPort;
    private final PatientRepositoryPort patientRepositoryPort;
    private final DentistRepositoryPort dentistRepositoryPort;

    @Override
    @Transactional
    public AppointmentResponse scheduleAppointment(AppointmentRequest request) {
        log.info("Scheduling appointment for patientId: {} with dentistId: {} at {}", request.patientId(), request.dentistId(), request.scheduledTime());

        PatientId patientId = new PatientId(UUID.fromString(request.patientId()));
        DentistId dentistId = new DentistId(UUID.fromString(request.dentistId()));

        Patient patient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + request.patientId()));
        Dentist dentist = dentistRepositoryPort.findById(dentistId)
                .orElseThrow(() -> new IllegalArgumentException("Dentist not found with id: " + request.dentistId()));

        validateDentistAvailability(dentistId, request.scheduledTime());

        Appointment appointment = Appointment.builder()
                .id(new AppointmentId(UUID.randomUUID()))
                .scheduledTime(request.scheduledTime())
                .status(AppointmentStatus.SCHEDULED)
                .dentist(dentist)
                .build();

        patient.addAppointment(appointment);
        dentist.addAppointment(appointment);

        Appointment savedAppointment = appointmentRepositoryPort.save(appointment);
        log.info("Appointment scheduled successfully with id: {}", savedAppointment.getId().value());

        return mapToResponse(savedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse confirmAppointment(UUID appointmentId) {
        log.info("Confirming appointment with id: {}", appointmentId);
        AppointmentId id = new AppointmentId(appointmentId);
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only SCHEDULED appointments can be confirmed. Current status: " + appointment.getStatus());
        }

        appointment.confirm();
        Appointment updatedAppointment = appointmentRepositoryPort.save(appointment);
        log.info("Appointment with id: {} confirmed successfully.", appointmentId);
        return mapToResponse(updatedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(UUID appointmentId) {
        log.info("Completing appointment with id: {}", appointmentId);
        AppointmentId id = new AppointmentId(appointmentId);
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED appointments can be completed. Current status: " + appointment.getStatus());
        }

        appointment.complete();
        Appointment updatedAppointment = appointmentRepositoryPort.save(appointment);
        log.info("Appointment with id: {} completed successfully.", appointmentId);
        return mapToResponse(updatedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(UUID appointmentId) {
        log.info("Cancelling appointment with id: {}", appointmentId);
        AppointmentId id = new AppointmentId(appointmentId);
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + appointmentId));

        appointment.cancel();
        Appointment updatedAppointment = appointmentRepositoryPort.save(appointment);
        log.info("Appointment with id: {} cancelled successfully.", appointmentId);
        return mapToResponse(updatedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(UUID appointmentId) {
        log.debug("Fetching appointment with id: {}", appointmentId);
        AppointmentId id = new AppointmentId(appointmentId);
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + appointmentId));
        return mapToResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatient(UUID patientId) {
        log.debug("Fetching appointments for patientId: {}", patientId);
        PatientId id = new PatientId(patientId);
        List<Appointment> appointments = appointmentRepositoryPort.findByPatientId(id);
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDentist(UUID dentistId) {
        log.debug("Fetching appointments for dentistId: {}", dentistId);
        DentistId id = new DentistId(dentistId);
        List<Appointment> appointments = appointmentRepositoryPort.findByDentistId(id);
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        log.debug("Fetching appointments with status: {}", status);
        List<Appointment> appointments = appointmentRepositoryPort.findByStatus(status);
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateDentistAvailability(DentistId dentistId, LocalDateTime scheduledTime) {
        List<Appointment> existingAppointments = appointmentRepositoryPort.findByDentistIdAndTimeRange(dentistId, scheduledTime, scheduledTime.plusHours(1));
        boolean hasConflict = existingAppointments.stream()
                .anyMatch(appt -> appt.getStatus() == AppointmentStatus.SCHEDULED || appt.getStatus() == AppointmentStatus.CONFIRMED);
        if (hasConflict) {
            throw new IllegalStateException("Dentist is already booked for the requested time slot.");
        }
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId().value().toString(),
                appointment.getScheduledTime(),
                appointment.getStatus(),
                appointment.getDentist().getId().value().toString(),
                appointment.getDentist().getName()
        );
    }
}