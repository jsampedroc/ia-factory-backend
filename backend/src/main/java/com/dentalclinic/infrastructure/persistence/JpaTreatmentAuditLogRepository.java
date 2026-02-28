package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.TreatmentAuditLog;
import com.dentalclinic.domain.valueobject.TreatmentAuditLogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTreatmentAuditLogRepository extends JpaRepository<TreatmentAuditLog, TreatmentAuditLogId> {
}