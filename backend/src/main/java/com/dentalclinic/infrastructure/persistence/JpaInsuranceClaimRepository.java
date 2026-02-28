package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.InsuranceClaim;
import com.dentalclinic.domain.valueobject.InsuranceClaimId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaInsuranceClaimRepository extends JpaRepository<InsuranceClaim, InsuranceClaimId> {
    // Spring Data JPA will automatically provide CRUD operations.
    // The repository works with the InsuranceClaim entity and its custom ID type InsuranceClaimId.
}