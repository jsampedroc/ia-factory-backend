package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.MedicalAlert;
import com.dentalclinic.domain.valueobject.MedicalAlertId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaMedicalAlertRepository extends JpaRepository<MedicalAlert, MedicalAlertId> {
}