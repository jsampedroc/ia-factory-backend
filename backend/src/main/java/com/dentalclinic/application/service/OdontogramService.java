package com.dentalclinic.application.service;

import com.dentalclinic.application.dto.OdontogramRequest;
import com.dentalclinic.application.dto.OdontogramResponse;
import com.dentalclinic.domain.model.ElectronicHealthRecord;
import com.dentalclinic.domain.model.Odontogram;
import com.dentalclinic.domain.valueobject.ElectronicHealthRecordId;
import com.dentalclinic.domain.valueobject.OdontogramId;
import com.dentalclinic.domain.repository.ElectronicHealthRecordRepository;
import com.dentalclinic.domain.repository.OdontogramRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OdontogramService {

    private final OdontogramRepository odontogramRepository;
    private final ElectronicHealthRecordRepository ehrRepository;

    @Transactional
    public OdontogramResponse createOdontogram(OdontogramRequest request) {
        log.info("Creating odontogram for EHR ID: {}", request.ehrId());

        ElectronicHealthRecord ehr = ehrRepository.findById(new ElectronicHealthRecordId(request.ehrId()))
                .orElseThrow(() -> new IllegalArgumentException("Electronic Health Record not found with ID: " + request.ehrId()));

        if (ehr.getOdontogram() != null) {
            throw new IllegalStateException("An Odontogram already exists for this Electronic Health Record.");
        }

        Odontogram odontogram = Odontogram.builder()
                .id(new OdontogramId(UUID.randomUUID()))
                .diagram(request.diagram())
                .build();

        ehr.setOdontogram(odontogram);
        odontogramRepository.save(odontogram);
        ehrRepository.save(ehr);

        log.info("Odontogram created with ID: {}", odontogram.getId().value());
        return mapToResponse(odontogram);
    }

    public OdontogramResponse getOdontogramById(UUID id) {
        log.info("Fetching odontogram with ID: {}", id);
        Odontogram odontogram = odontogramRepository.findById(new OdontogramId(id))
                .orElseThrow(() -> new IllegalArgumentException("Odontogram not found with ID: " + id));
        return mapToResponse(odontogram);
    }

    @Transactional
    public OdontogramResponse updateOdontogram(UUID id, OdontogramRequest request) {
        log.info("Updating odontogram with ID: {}", id);
        Odontogram odontogram = odontogramRepository.findById(new OdontogramId(id))
                .orElseThrow(() -> new IllegalArgumentException("Odontogram not found with ID: " + id));

        odontogram.setDiagram(request.diagram());
        odontogramRepository.save(odontogram);

        log.info("Odontogram updated with ID: {}", id);
        return mapToResponse(odontogram);
    }

    @Transactional
    public void deleteOdontogram(UUID id) {
        log.info("Deleting odontogram with ID: {}", id);
        Odontogram odontogram = odontogramRepository.findById(new OdontogramId(id))
                .orElseThrow(() -> new IllegalArgumentException("Odontogram not found with ID: " + id));

        ElectronicHealthRecord ehr = ehrRepository.findByOdontogramId(new OdontogramId(id))
                .orElseThrow(() -> new IllegalStateException("Associated Electronic Health Record not found for Odontogram ID: " + id));

        ehr.setOdontogram(null);
        ehrRepository.save(ehr);
        odontogramRepository.delete(odontogram);

        log.info("Odontogram deleted with ID: {}", id);
    }

    private OdontogramResponse mapToResponse(Odontogram odontogram) {
        return new OdontogramResponse(
                odontogram.getId().value(),
                odontogram.getDiagram()
        );
    }
}