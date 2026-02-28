package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Appointment;
import com.dentalclinic.domain.valueobject.AppointmentId;
import com.dentalclinic.domain.valueobject.DentistId;
import com.dentalclinic.domain.valueobject.PatientId;
import com.dentalclinic.domain.shared.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<Appointment, AppointmentId> {

    List<Appointment> findByPatientId(PatientId patientId);

    List<Appointment> findByDentistId(DentistId dentistId);

    List<Appointment> findByStatus(AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.dentist.id = :dentistId AND a.scheduledTime = :scheduledTime")
    Optional<Appointment> findByDentistIdAndScheduledTime(@Param("dentistId") DentistId dentistId, @Param("scheduledTime") LocalDateTime scheduledTime);

    @Query("SELECT a FROM Appointment a WHERE a.dentist.id = :dentistId AND a.scheduledTime BETWEEN :start AND :end")
    List<Appointment> findByDentistIdAndScheduledTimeBetween(@Param("dentistId") DentistId dentistId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.scheduledTime BETWEEN :start AND :end")
    List<Appointment> findByPatientIdAndScheduledTimeBetween(@Param("patientId") PatientId patientId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}