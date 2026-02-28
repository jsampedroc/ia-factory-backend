package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Consent;
import com.dentalclinic.domain.valueobject.ConsentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaConsentRepository extends JpaRepository<Consent, ConsentId> {
}