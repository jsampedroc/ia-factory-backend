package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.ElectronicHealthRecord;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaElectronicHealthRecordRepository extends JpaRepository<ElectronicHealthRecord, ElectronicHealthRecordId> {
}