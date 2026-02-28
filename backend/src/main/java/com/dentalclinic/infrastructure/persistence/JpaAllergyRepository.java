package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Allergy;
import com.dentalclinic.domain.valueobject.AllergyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaAllergyRepository extends JpaRepository<Allergy, AllergyId> {
}