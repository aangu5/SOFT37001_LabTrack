package org.ordep.labtrack.service;

import org.ordep.labtrack.configuration.LabTrackUtilities;
import org.ordep.labtrack.data.CoshhAssessmentRepository;
import org.ordep.labtrack.data.RiskAssessmentRepository;
import org.ordep.labtrack.exception.AssessmentNotFoundException;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.AssessmentState;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssessmentService {
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final CoshhAssessmentRepository coshhAssessmentRepository;
    private final UserService userService;

    public AssessmentService(RiskAssessmentRepository riskAssessmentRepository, CoshhAssessmentRepository coshhAssessmentRepository, UserService userService){
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.coshhAssessmentRepository = coshhAssessmentRepository;
        this.userService = userService;
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
        riskAssessment.setStatus(AssessmentState.PENDING);

        riskAssessmentRepository.save(riskAssessment);

        return riskAssessment;
    }

    public RiskAssessment findOneRiskAssessment(UUID assessmentId) {
        Optional<RiskAssessment> optional = riskAssessmentRepository.findById(assessmentId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new AssessmentNotFoundException(assessmentId);
    }

    public List<RiskAssessment> findAllRiskAssessmentsToApprove() {

        LabTrackUser currentUser = userService.getCurrentUser();

        if (LabTrackUtilities.canUserApprove(currentUser)) {
            return riskAssessmentRepository.findAllByStatus(AssessmentState.PENDING);
        } else {
            return new ArrayList<>();
        }
    }

    public void updateRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessmentRepository.save(riskAssessment);
    }

    public void deleteRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessmentRepository.delete(riskAssessment);
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
        coshhAssessment.setStatus(AssessmentState.PENDING);

        coshhAssessmentRepository.save(coshhAssessment);

        return coshhAssessment;
    }

    public List<CoshhAssessment> findAllCoshhAssessmentsToApprove() {
        LabTrackUser currentUser = userService.getCurrentUser();

        if (LabTrackUtilities.canUserApprove(currentUser)) {
            return coshhAssessmentRepository.findAllByStatus(AssessmentState.PENDING);
        } else {
            return new ArrayList<>();
        }
    }

    public CoshhAssessment findOneCoshhAssessment(UUID assessmentId) {
        Optional<CoshhAssessment> optional = coshhAssessmentRepository.findById(assessmentId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new AssessmentNotFoundException(assessmentId);
    }

    public void deleteCoshhAssessment(CoshhAssessment coshhAssessment) {
        coshhAssessmentRepository.delete(coshhAssessment);
    }


    public boolean canUserSignAssessment(CoshhAssessment assessment, LabTrackUser user) {
        return (!LabTrackUtilities.hasUserSignedAssessment(assessment, user) && assessment.getStatus() == AssessmentState.APPROVED);
    }

    public boolean canUserSignAssessment(UUID assessmentId) {
        var assessment = findOneCoshhAssessment(assessmentId);
        return canUserSignAssessment(assessment, userService.getCurrentUser());
    }

    public void updateCoshhAssessment(CoshhAssessment coshhAssessment) {
        coshhAssessmentRepository.save(coshhAssessment);
    }

    public List<Assessment> getAllAssessments() {
        List<Assessment> assessments = new ArrayList<>();

        assessments.addAll(riskAssessmentRepository.findAll());
        assessments.addAll(coshhAssessmentRepository.findAll());

        assessments.sort(Comparator.comparing(Assessment::getDateCreated).reversed());

        return assessments;
    }

    public List<RiskAssessment> searchRiskAssessmentAuthor(String author) {
        return riskAssessmentRepository.findByAuthor_DisplayNameContainsIgnoreCase(author);
    }

    public List<RiskAssessment> searchRiskAssessmentName(String name) {
        return riskAssessmentRepository.findByAssessmentNameContainsIgnoreCase(name);
    }

    public List<CoshhAssessment> searchCoshhAssessmentAuthor(String author) {
        return coshhAssessmentRepository.findByAuthor_DisplayNameContainsIgnoreCase(author);
    }

    public List<CoshhAssessment> searchCoshhAssessmentName(String name) {
        return coshhAssessmentRepository.findByAssessmentNameContainsIgnoreCase(name);
    }
}
