package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.DentistRequest;
import com.dentalclinic.application.dto.DentistResponse;
import com.dentalclinic.application.mapper.DentistMapper;
import com.dentalclinic.domain.model.Dentist;
import com.dentalclinic.domain.valueobject.DentistId;
import com.dentalclinic.domain.repository.DentistRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DentistService {

    private final DentistRepository dentistRepository;
    private final DentistMapper dentistMapper;

    public DentistResponse createDentist(DentistRequest dentistRequest) {
        log.info("Creating new dentist with name: {}", dentistRequest.name());
        Dentist dentist = dentistMapper.toEntity(dentistRequest);
        Dentist savedDentist = dentistRepository.save(dentist);
        log.info("Dentist created with ID: {}", savedDentist.getId().value());
        return dentistMapper.toResponse(savedDentist);
    }

    public DentistResponse getDentistById(UUID id) {
        log.debug("Fetching dentist with ID: {}", id);
        DentistId dentistId = new DentistId(id);
        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new EntityNotFoundException("Dentist not found with ID: " + id));
        return dentistMapper.toResponse(dentist);
    }

    public List<DentistResponse> getAllDentists() {
        log.debug("Fetching all dentists");
        return dentistRepository.findAll().stream()
                .map(dentistMapper::toResponse)
                .collect(Collectors.toList());
    }

    public DentistResponse updateDentist(UUID id, DentistRequest dentistRequest) {
        log.info("Updating dentist with ID: {}", id);
        DentistId dentistId = new DentistId(id);
        Dentist existingDentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new EntityNotFoundException("Dentist not found with ID: " + id));

        Dentist updatedDentist = dentistMapper.partialUpdate(dentistRequest, existingDentist);
        Dentist savedDentist = dentistRepository.save(updatedDentist);
        log.info("Dentist updated with ID: {}", savedDentist.getId().value());
        return dentistMapper.toResponse(savedDentist);
    }

    public void deleteDentist(UUID id) {
        log.info("Deleting dentist with ID: {}", id);
        DentistId dentistId = new DentistId(id);
        if (!dentistRepository.existsById(dentistId)) {
            throw new EntityNotFoundException("Dentist not found with ID: " + id);
        }
        dentistRepository.deleteById(dentistId);
        log.info("Dentist deleted with ID: {}", id);
    }
}