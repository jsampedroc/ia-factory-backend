package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Profile;
import com.dentalclinic.domain.valueobject.ProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaProfileRepository extends JpaRepository<Profile, UUID> {
}