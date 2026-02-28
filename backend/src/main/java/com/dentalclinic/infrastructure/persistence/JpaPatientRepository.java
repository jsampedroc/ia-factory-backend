package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Patient;
import com.dentalclinic.domain.valueobject.PatientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findById(PatientId id);

    @Query("SELECT p FROM Patient p WHERE p.name = :name AND p.dateOfBirth = :dateOfBirth")
    List<Patient> findByNameAndDateOfBirth(@Param("name") String name, @Param("dateOfBirth") LocalDate dateOfBirth);

    @Query("SELECT p FROM Patient p WHERE p.name LIKE %:name%")
    List<Patient> findByNameContaining(@Param("name") String name);

    boolean existsById(PatientId id);

    void deleteById(PatientId id);
}