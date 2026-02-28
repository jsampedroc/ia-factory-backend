package com.dentalclinic.application.service;

import com.dentalclinic.domain.model.Role;
import com.dentalclinic.domain.valueobject.RoleId;
import com.dentalclinic.application.dto.RoleRequest;
import com.dentalclinic.application.dto.RoleResponse;
import com.dentalclinic.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        log.info("Creating new role with name: {}", request.roleName());
        Role role = Role.builder()
                .roleName(request.roleName())
                .build();
        Role savedRole = roleRepository.save(role);
        log.info("Role created with ID: {}", savedRole.getId());
        return mapToResponse(savedRole);
    }

    public Optional<RoleResponse> getRoleById(UUID id) {
        log.debug("Fetching role by ID: {}", id);
        return roleRepository.findById(new RoleId(id))
                .map(this::mapToResponse);
    }

    public List<RoleResponse> getAllRoles() {
        log.debug("Fetching all roles");
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<RoleResponse> updateRole(UUID id, RoleRequest request) {
        log.info("Updating role with ID: {}", id);
        RoleId roleId = new RoleId(id);
        return roleRepository.findById(roleId)
                .map(existingRole -> {
                    existingRole.setRoleName(request.roleName());
                    Role updatedRole = roleRepository.save(existingRole);
                    log.info("Role updated with ID: {}", updatedRole.getId());
                    return mapToResponse(updatedRole);
                });
    }

    @Transactional
    public boolean deleteRole(UUID id) {
        log.info("Deleting role with ID: {}", id);
        RoleId roleId = new RoleId(id);
        if (roleRepository.existsById(roleId)) {
            roleRepository.deleteById(roleId);
            log.info("Role deleted with ID: {}", id);
            return true;
        }
        log.warn("Role not found for deletion with ID: {}", id);
        return false;
    }

    private RoleResponse mapToResponse(Role role) {
        return new RoleResponse(
                role.getId().value(),
                role.getRoleName()
        );
    }
}