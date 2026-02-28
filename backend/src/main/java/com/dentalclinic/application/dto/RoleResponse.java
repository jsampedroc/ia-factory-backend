package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.RoleId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    @NotNull
    private UUID id;

    @NotBlank
    private String roleName;
}