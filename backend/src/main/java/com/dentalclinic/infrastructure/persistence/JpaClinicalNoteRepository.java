package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.ClinicalNote;
import com.dentalclinic.domain.valueobject.ClinicalNoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaClinicalNoteRepository extends JpaRepository<ClinicalNote, ClinicalNoteId> {
}