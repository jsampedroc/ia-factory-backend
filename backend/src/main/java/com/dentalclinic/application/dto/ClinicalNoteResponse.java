package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.ClinicalNoteId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalNoteResponse {
    @NotNull
    private UUID id;

    @NotBlank
    private String note;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private UUID electronicHealthRecordId;
}