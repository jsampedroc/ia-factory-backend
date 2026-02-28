package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Dentist;
import com.dentalclinic.domain.valueobject.DentistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaDentistRepository extends JpaRepository<Dentist, DentistId> {
    Optional<Dentist> findById(DentistId id);
}