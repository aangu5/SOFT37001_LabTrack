package org.ordep.labtrack.service;

import org.ordep.labtrack.data.CoshhAssessmentRepository;
import org.ordep.labtrack.data.RiskAssessmentRepository;
import org.ordep.labtrack.exception.CardNotFoundException;
import org.ordep.labtrack.model.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssessmentService {
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final CoshhAssessmentRepository coshhAssessmentRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AssessmentService(RiskAssessmentRepository riskAssessmentRepository, CoshhAssessmentRepository coshhAssessmentRepository, UserService userService, AuthenticationService authenticationService){
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.coshhAssessmentRepository = coshhAssessmentRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public List<RiskAssessment> findAllRiskAssessmentsForUser(UUID userID) {
        LabTrackUser currentUser = userService.findUser(userID);
        return riskAssessmentRepository.findRiskAssessmentByAuthor(currentUser);
    }

    public List<RiskAssessment> getAllRiskAssessments(){
        List<RiskAssessment> output = riskAssessmentRepository.findAll();
        output.sort(Comparator.comparing(RiskAssessment::getDateCreated).reversed());
        return output;
    }

    public RiskAssessment newRiskAssessment(RiskAssessment riskAssessment) {

        var riskAssessmentID = UUID.randomUUID();
        LabTrackUser author = userService.getCurrentUser();

        riskAssessment.setAssessmentId(riskAssessmentID);
        riskAssessment.setAuthor(author);
        riskAssessment.setDateCreated(LocalDateTime.now());

        riskAssessmentRepository.save(riskAssessment);

        return riskAssessment;
    }

    public RiskAssessment findOneRiskAssessment(UUID assessmentId) {
        Optional<RiskAssessment> optional = riskAssessmentRepository.findById(assessmentId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CardNotFoundException(assessmentId);
    }

    public List<RiskAssessment> findAllRiskAssessmentsToApprove() {

        LabTrackUser currentUser = userService.getCurrentUser();

        if (authenticationService.canUserApprove(currentUser)) {
            return riskAssessmentRepository.findAllByApproved(false);
        } else {
            return new ArrayList<>();
        }
    }

    public void updateRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessmentRepository.save(riskAssessment);
    }

    public List<CoshhAssessment> findAllCoshhAssessmentsForUser(UUID userID) {
        LabTrackUser currentUser = userService.findUser(userID);
        return coshhAssessmentRepository.findCoshhAssessmentByAuthor(currentUser);
    }

    public List<CoshhAssessment> getAllCoshhAssessments(){
        List<CoshhAssessment> output = coshhAssessmentRepository.findAll();
        output.sort(Comparator.comparing(CoshhAssessment::getDateCreated).reversed());
        return output;
    }

    public CoshhAssessment newCoshhAssessment(CoshhAssessment coshhAssessment) {

        var riskAssessmentID = UUID.randomUUID();
        LabTrackUser author = userService.getCurrentUser();

        coshhAssessment.setAssessmentId(riskAssessmentID);
        coshhAssessment.setAuthor(author);
        coshhAssessment.setDateCreated(LocalDateTime.now());

        coshhAssessmentRepository.save(coshhAssessment);

        return coshhAssessment;
    }

    public List<CoshhAssessment> findAllCoshhAssessmentsToApprove() {
        LabTrackUser currentUser = userService.getCurrentUser();

        if (authenticationService.canUserApprove(currentUser)) {
            return coshhAssessmentRepository.findAllByApproved(false);
        } else {
            return new ArrayList<>();
        }
    }
}
