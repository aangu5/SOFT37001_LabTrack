package org.ordep.labtrack.data;

import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.RiskAssessment;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface RiskAssessmentRepository extends PagingAndSortingRepository<RiskAssessment, UUID> {
    List<RiskAssessment> findRiskAssessmentByAuthor(LabTrackUser author);
    List<RiskAssessment> findAll();
}
