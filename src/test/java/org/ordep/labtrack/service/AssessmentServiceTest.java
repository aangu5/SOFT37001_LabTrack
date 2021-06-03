package org.ordep.labtrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.CoshhAssessmentRepository;
import org.ordep.labtrack.data.RiskAssessmentRepository;
import org.ordep.labtrack.exception.AssessmentNotFoundException;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.CoshhAssessment;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.RiskAssessment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    private final UUID uuid = UUID.fromString("702d1aab-fd01-400f-9d08-fb6485b0a773");

    @Mock
    private RiskAssessmentRepository riskAssessmentRepository;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private CoshhAssessmentRepository coshhAssessmentRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private AssessmentService assessmentService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void findAllRiskAssessmentsForUser() throws IOException {
        LabTrackUser user = new LabTrackUser(uuid, "display name", "email@mail.com", false);
        when(userService.findUser(any(UUID.class))).thenReturn(user);

        RiskAssessment riskAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);
        when(riskAssessmentRepository.findRiskAssessmentByAuthor(any())).thenReturn(Collections.singletonList(riskAssessment));

        Assertions.assertEquals(Collections.singletonList(riskAssessment), assessmentService.findAllRiskAssessmentsForUser(uuid));
    }

    @Test
    void getAllRiskAssessments() throws IOException {
        RiskAssessment riskAssessment = objectMapper.readValue(new ClassPathResource(
                        "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);
        when(riskAssessmentRepository.findAll()).thenReturn(Collections.singletonList(riskAssessment));

        Assertions.assertEquals(Collections.singletonList(riskAssessment), assessmentService.getAllRiskAssessments());
    }

    @Test
    void newRiskAssessment() throws IOException {
        LabTrackUser user = new LabTrackUser(uuid, "display name", "email@mail.com", false);
        when(userService.getCurrentUser()).thenReturn(user);

        RiskAssessment riskAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);
        assertNotNull(assessmentService.newRiskAssessment(riskAssessment));
    }

    @Test
    void findOneRiskAssessment() throws IOException {
        RiskAssessment riskAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);

        when(riskAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(riskAssessment));

        assertEquals(riskAssessment, assessmentService.findOneRiskAssessment(uuid));

    }

    @Test
    void findOneRiskAssessment_empty() {
        when(riskAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class, () -> assessmentService.findOneRiskAssessment(uuid));
    }

    @Test
    void findAllRiskAssessmentsToApprove() throws IOException {
        RiskAssessment riskAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);

        when(riskAssessmentRepository.findAllByApproved(false)).thenReturn(Collections.singletonList(riskAssessment));
        when(authenticationService.canUserApprove(any())).thenReturn(true);

        assertEquals(Collections.singletonList(riskAssessment), assessmentService.findAllRiskAssessmentsToApprove());

    }

    @Test
    void findAllRiskAssessmentsToApprove_unableToApprove() {

        when(authenticationService.canUserApprove(any())).thenReturn(false);

        assertEquals(Collections.emptyList(), assessmentService.findAllRiskAssessmentsToApprove());
    }

    @Test
    void updateRiskAssessment() {
        assessmentService.updateRiskAssessment(new RiskAssessment());
        verify(riskAssessmentRepository, times(1)).save(any());
    }

    @Test
    void findAllCoshhAssessmentsForUser() throws IOException {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        when(userService.findUser(any(UUID.class))).thenReturn(new LabTrackUser());
        when(coshhAssessmentRepository.findCoshhAssessmentByAuthor(any(LabTrackUser.class))).thenReturn(Collections.singletonList(coshhAssessment));

        assertEquals(Collections.singletonList(coshhAssessment), assessmentService.findAllCoshhAssessmentsForUser(uuid));
    }

    @Test
    void getAllCoshhAssessments() throws IOException {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        when(coshhAssessmentRepository.findAll()).thenReturn(Collections.singletonList(coshhAssessment));

        assertEquals(Collections.singletonList(coshhAssessment), assessmentService.getAllCoshhAssessments());
    }

    @Test
    void newCoshhAssessment() throws IOException {
        LabTrackUser user = new LabTrackUser(uuid, "display name", "email@mail.com", false);
        when(userService.getCurrentUser()).thenReturn(user);

        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        assertNotNull(assessmentService.newCoshhAssessment(coshhAssessment));
        verify(coshhAssessmentRepository, times(1)).save(any());
    }

    @Test
    void findAllCoshhAssessmentsToApprove() throws IOException {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        when(coshhAssessmentRepository.findAllByApproved(false)).thenReturn(Collections.singletonList(coshhAssessment));
        when(authenticationService.canUserApprove(any())).thenReturn(true);

        assertEquals(Collections.singletonList(coshhAssessment), assessmentService.findAllCoshhAssessmentsToApprove());

    }

    @Test
    void findAllCoshhAssessmentsToApprove_unableToApprove() {

        when(authenticationService.canUserApprove(any())).thenReturn(false);

        assertEquals(Collections.emptyList(), assessmentService.findAllCoshhAssessmentsToApprove());
    }

    @Test
    void findOneCoshhAssessment() throws IOException {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        when(coshhAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(coshhAssessment));

        assertEquals(coshhAssessment, assessmentService.findOneCoshhAssessment(uuid));

    }

    @Test
    void findOneCoshhAssessment_empty() {

        assertThrows(AssessmentNotFoundException.class, () -> assessmentService.findOneCoshhAssessment(uuid));
    }

    @Test
    void testCanUserSignAssessment_true() throws IOException {

        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(coshhAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(coshhAssessment));
        when(userService.getCurrentUser()).thenReturn(user);

        assertFalse(assessmentService.canUserSignAssessment(uuid));

    }

    @Test
    void testCanUserSignAssessment_false() throws IOException {

        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(coshhAssessmentRepository.findById(any())).thenReturn(Optional.of(coshhAssessment));
        when(userService.getCurrentUser()).thenReturn(user);

        coshhAssessment.setApproved(false);
        coshhAssessment.setSignedUsers(Collections.emptyList());

        assertFalse(assessmentService.canUserSignAssessment(uuid));
    }

    @Test
    void hasUserSignedAssessment() throws IOException {

        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        assertTrue(assessmentService.hasUserSignedAssessment(coshhAssessment, user));

        user.setEmailAddress("test@gmail.com");

        assertFalse(assessmentService.hasUserSignedAssessment(coshhAssessment, user));

    }

    @Test
    void updateCoshhAssessment() {
        assessmentService.updateCoshhAssessment(new CoshhAssessment());
        verify(coshhAssessmentRepository, times(1)).save(any());
    }
}