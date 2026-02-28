package com.dentalclinic.application.dto;

import com.dentalclinic.domain.valueobject.ConsentId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record ConsentResponse(
        @NotNull
        UUID id,
        @NotBlank
        String description,
        @NotNull
        LocalDate dateGiven
) {
    public static ConsentResponse fromDomain(com.dentalclinic.domain.model.Consent consent) {
        return new ConsentResponse(
                consent.getId().value(),
                consent.getDescription(),
                consent.getDateGiven()
        );
    }
}