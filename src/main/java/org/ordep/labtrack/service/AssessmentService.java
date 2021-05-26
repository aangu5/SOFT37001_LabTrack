package org.ordep.labtrack.service;

import org.ordep.labtrack.data.RiskAssessmentRepository;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.RiskAssessment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssessmentService {
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final UserService userService;

    public AssessmentService(RiskAssessmentRepository riskAssessmentRepository, UserService userService){
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.userService = userService;
    }

    public List<RiskAssessment> findAllRiskAssessmentsForUser(UUID userID) {
        LabTrackUser currentUser = userService.findUser(userID);
        return riskAssessmentRepository.findRiskAssessmentByAuthor(currentUser);
    }

    public List<RiskAssessment> getAllRiskAssessments(){
        return riskAssessmentRepository.findAll();
    }

    public RiskAssessment newRiskAssessment(RiskAssessment input) {

        var riskAssessmentID = UUID.randomUUID();
        LabTrackUser author = userService.getCurrentUser();

        var riskAssessment = new RiskAssessment();
        riskAssessment.setAssessmentId(riskAssessmentID);
        riskAssessment.setAssessmentName(input.getAssessmentName());
        riskAssessment.setAuthor(author);
        riskAssessment.setReaction(input.getReaction());
        riskAssessment.setRisks(input.getRisks());

        return riskAssessment;
    }
}
