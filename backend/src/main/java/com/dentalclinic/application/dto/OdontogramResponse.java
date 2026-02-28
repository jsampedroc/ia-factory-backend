package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.OdontogramId;
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
public class OdontogramResponse {
    @NotNull
    private UUID id;
    @NotNull
    private String diagram;
}