package com.dentalclinic.domain.model;

import com.dentalclinic.domain.shared.Entity;
import com.dentalclinic.domain.valueobject.ClinicalNoteId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClinicalNote extends Entity<ClinicalNoteId> {

    @NotBlank(message = "Clinical note content cannot be blank")
    @Size(max = 2000, message = "Clinical note cannot exceed 2000 characters")
    private String note;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

    // Business methods can be added here
    public void updateNote(String updatedNote) {
        if (updatedNote != null && !updatedNote.isBlank()) {
            this.note = updatedNote;
            this.timestamp = LocalDateTime.now(); // Update timestamp on modification
        }
    }
}