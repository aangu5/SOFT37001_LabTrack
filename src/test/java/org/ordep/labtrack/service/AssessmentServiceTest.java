package org.ordep.labtrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.RiskAssessmentRepository;
import org.ordep.labtrack.model.LabTrackUser;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    private final UUID userID = UUID.fromString("702d1aab-fd01-400f-9d08-fb6485b0a773");

    @Mock
    private RiskAssessmentRepository riskAssessmentRepository;
    @Mock
    private UserService userService;

    @Test
    void findAllRiskAssessmentsForUser() {
        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", false);
        Mockito.when(userService.findUser(Mockito.any(UUID.class))).thenReturn(user);
    }
}