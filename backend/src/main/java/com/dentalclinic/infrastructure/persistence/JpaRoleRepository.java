package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Role;
import com.dentalclinic.domain.valueobject.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, RoleId> {
    Optional<Role> findByRoleName(String roleName);
}