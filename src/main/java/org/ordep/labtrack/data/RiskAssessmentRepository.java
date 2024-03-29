package org.ordep.labtrack.data;

import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.RiskAssessment;
import org.ordep.labtrack.model.enums.AssessmentState;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface RiskAssessmentRepository extends PagingAndSortingRepository<RiskAssessment, UUID> {
    List<RiskAssessment> findRiskAssessmentByAuthor(LabTrackUser author);
    List<RiskAssessment> findAll();
    List<RiskAssessment> findAllByStatus(AssessmentState state);
    List<RiskAssessment> findByAssessmentNameContainsIgnoreCase(String cardName);
    List<RiskAssessment> findByAuthor_DisplayNameContainsIgnoreCase(String displayName);
}
