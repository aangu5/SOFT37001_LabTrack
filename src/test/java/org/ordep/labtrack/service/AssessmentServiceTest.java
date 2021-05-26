package org.ordep.labtrack.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.RiskAssessmentRepository;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.RiskAssessment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    private final UUID userID = UUID.fromString("702d1aab-fd01-400f-9d08-fb6485b0a773");

    @Mock
    private RiskAssessmentRepository riskAssessmentRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private AssessmentService assessmentService;

    @Test
    void findAllRiskAssessmentsForUser() {
        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", false);
        Mockito.when(userService.findUser(Mockito.any(UUID.class))).thenReturn(user);

        RiskAssessment riskAssessment = new RiskAssessment();
        Mockito.when(riskAssessmentRepository.findRiskAssessmentByAuthor(Mockito.any())).thenReturn(Collections.singletonList(riskAssessment));

        Assertions.assertEquals(Collections.singletonList(riskAssessment), assessmentService.findAllRiskAssessmentsForUser(userID));
    }

    @Test
    void getAllRiskAssessments() {
        RiskAssessment riskAssessment = new RiskAssessment();
        Mockito.when(riskAssessmentRepository.findAll()).thenReturn(Collections.singletonList(riskAssessment));

        Assertions.assertEquals(Collections.singletonList(riskAssessment), assessmentService.getAllRiskAssessments());
    }

    @Test
    void newRiskAssessment() {
        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", false);
        Mockito.when(userService.getCurrentUser()).thenReturn(user);

        RiskAssessment riskAssessment = new RiskAssessment();
        assertNotNull(assessmentService.newRiskAssessment(riskAssessment));
    }

}