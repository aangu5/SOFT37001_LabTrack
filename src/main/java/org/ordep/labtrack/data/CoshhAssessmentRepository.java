package org.ordep.labtrack.data;

import org.ordep.labtrack.model.CoshhAssessment;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.AssessmentState;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface CoshhAssessmentRepository extends PagingAndSortingRepository<CoshhAssessment, UUID> {
    List<CoshhAssessment> findCoshhAssessmentByAuthor(LabTrackUser author);
    List<CoshhAssessment> findAll();
    List<CoshhAssessment> findAllByStatus(AssessmentState state);
    List<CoshhAssessment> findByAuthor_DisplayNameContainsIgnoreCase(String author);
    List<CoshhAssessment> findByAssessmentNameContainsIgnoreCase(String name);
}
