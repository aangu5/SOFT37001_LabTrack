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
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.AssessmentState;
import org.ordep.labtrack.model.enums.Role;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    private final UUID uuid = UUID.fromString("702d1aab-fd01-400f-9d08-fb6485b0a773");

    @Mock
    private RiskAssessmentRepository riskAssessmentRepository;
    @Mock
    private UserService userService;
    @Mock
    private CoshhAssessmentRepository coshhAssessmentRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private CoshhAssessment coshhAssessment;
    private RiskAssessment riskAssessment;
    private LabTrackUser user;

    @InjectMocks
    private AssessmentService assessmentService;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        coshhAssessment = objectMapper.readValue(new ClassPathResource("/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);
        user = objectMapper.readValue(new ClassPathResource("/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);
        riskAssessment = objectMapper.readValue(new ClassPathResource("/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);
    }

    @Test
    void findAllRiskAssessmentsForUser() {

        when(userService.findUser(any(UUID.class))).thenReturn(user);

        when(riskAssessmentRepository.findRiskAssessmentByAuthor(any())).thenReturn(Collections.singletonList(riskAssessment));

        Assertions.assertEquals(Collections.singletonList(riskAssessment), assessmentService.findAllRiskAssessmentsForUser(uuid));
    }

    @Test
    void getAllRiskAssessments() {

        when(riskAssessmentRepository.findAll()).thenReturn(Collections.singletonList(riskAssessment));

        Assertions.assertEquals(Collections.singletonList(riskAssessment), assessmentService.getAllRiskAssessments());
    }

    @Test
    void newRiskAssessment() {
        when(userService.getCurrentUser()).thenReturn(user);

        assertNotNull(assessmentService.newRiskAssessment(riskAssessment));
    }

    @Test
    void findOneRiskAssessment() {
        when(riskAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(riskAssessment));

        assertEquals(riskAssessment, assessmentService.findOneRiskAssessment(uuid));

    }

    @Test
    void findOneRiskAssessment_empty() {
        when(riskAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class, () -> assessmentService.findOneRiskAssessment(uuid));
    }

    @Test
    void findAllRiskAssessmentsToApprove() {
        when(riskAssessmentRepository.findAllByStatus(AssessmentState.PENDING)).thenReturn(Collections.singletonList(riskAssessment));
        when(userService.getCurrentUser()).thenReturn(user);

        assertEquals(Collections.singletonList(riskAssessment), assessmentService.findAllRiskAssessmentsToApprove());

    }

    @Test
    void findAllRiskAssessmentsToApprove_unableToApprove() {
        user.setRoles(Collections.singletonList(Role.STUDENT));
        when(userService.getCurrentUser()).thenReturn(user);

        assertEquals(Collections.emptyList(), assessmentService.findAllRiskAssessmentsToApprove());
    }

    @Test
    void updateRiskAssessment() {
        assessmentService.updateRiskAssessment(riskAssessment);
        verify(riskAssessmentRepository, times(1)).save(any());
    }

    @Test
    void findAllCoshhAssessmentsForUser() {
        when(userService.findUser(any(UUID.class))).thenReturn(user);
        when(coshhAssessmentRepository.findCoshhAssessmentByAuthor(any(LabTrackUser.class))).thenReturn(Collections.singletonList(coshhAssessment));

        assertEquals(Collections.singletonList(coshhAssessment), assessmentService.findAllCoshhAssessmentsForUser(uuid));
    }

    @Test
    void getAllCoshhAssessments() {
        when(coshhAssessmentRepository.findAll()).thenReturn(Collections.singletonList(coshhAssessment));

        assertEquals(Collections.singletonList(coshhAssessment), assessmentService.getAllCoshhAssessments());
    }

    @Test
    void newCoshhAssessment() {
        when(userService.getCurrentUser()).thenReturn(user);

        assertNotNull(assessmentService.newCoshhAssessment(coshhAssessment));
        verify(coshhAssessmentRepository, times(1)).save(any());
    }

    @Test
    void findAllCoshhAssessmentsToApprove() {
        when(coshhAssessmentRepository.findAllByStatus(AssessmentState.PENDING)).thenReturn(Collections.singletonList(coshhAssessment));
        when(userService.getCurrentUser()).thenReturn(user);

        assertEquals(Collections.singletonList(coshhAssessment), assessmentService.findAllCoshhAssessmentsToApprove());

    }

    @Test
    void findAllCoshhAssessmentsToApprove_unableToApprove() {
        user.setRoles(Collections.singletonList(Role.STUDENT));
        when(userService.getCurrentUser()).thenReturn(user);

        assertEquals(Collections.emptyList(), assessmentService.findAllCoshhAssessmentsToApprove());
    }

    @Test
    void findOneCoshhAssessment() {

        when(coshhAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(coshhAssessment));

        assertEquals(coshhAssessment, assessmentService.findOneCoshhAssessment(uuid));

    }

    @Test
    void findOneCoshhAssessment_empty() {

        assertThrows(AssessmentNotFoundException.class, () -> assessmentService.findOneCoshhAssessment(uuid));
    }

    @Test
    void testCanUserSignAssessment_true() {

        when(coshhAssessmentRepository.findById(any(UUID.class))).thenReturn(Optional.of(coshhAssessment));
        when(userService.getCurrentUser()).thenReturn(user);

        assertFalse(assessmentService.canUserSignAssessment(uuid));

    }

    @Test
    void testCanUserSignAssessment_false() {

        when(coshhAssessmentRepository.findById(any())).thenReturn(Optional.of(coshhAssessment));
        when(userService.getCurrentUser()).thenReturn(user);

        coshhAssessment.setStatus(AssessmentState.PENDING);
        coshhAssessment.setSignedUsers(Collections.emptyList());

        assertFalse(assessmentService.canUserSignAssessment(uuid));
    }

    @Test
    void updateCoshhAssessment() {
        assessmentService.updateCoshhAssessment(new CoshhAssessment());
        verify(coshhAssessmentRepository, times(1)).save(any());
    }

    @Test
    void deleteRiskAssessment() {
        assessmentService.deleteRiskAssessment(riskAssessment);
        verify(riskAssessmentRepository, times(1)).delete(any());
    }

    @Test
    void deleteCoshhAssessment() {
        assessmentService.deleteCoshhAssessment(coshhAssessment);
        verify(coshhAssessmentRepository, times(1)).delete(any());
    }

    @Test
    void getAllAssessments() {
        List<Assessment> expected = new ArrayList<>();
        expected.add(riskAssessment);
        expected.add(coshhAssessment);

        when(riskAssessmentRepository.findAll()).thenReturn(Collections.singletonList(riskAssessment));
        when(coshhAssessmentRepository.findAll()).thenReturn(Collections.singletonList(coshhAssessment));

        expected.sort(Comparator.comparing(Assessment::getDateCreated).reversed());

        List<Assessment> actual = assessmentService.getAllAssessments();

        assertEquals(expected, actual);
    }

    @Test
    void searchRiskAssessmentAuthor() {
        List<RiskAssessment> riskAssessments = Collections.singletonList(riskAssessment);

        List<RiskAssessment> expected = new ArrayList<>(riskAssessments);

        when(riskAssessmentRepository.findByAuthor_DisplayNameContainsIgnoreCase(anyString())).thenReturn(riskAssessments);

        expected.sort(Comparator.comparing(Assessment::getDateCreated).reversed());

        List<RiskAssessment> actual = assessmentService.searchRiskAssessmentAuthor("search term");

        assertEquals(expected, actual);
    }

    @Test
    void searchRiskAssessmentName() {
        List<RiskAssessment> riskAssessments = Collections.singletonList(riskAssessment);

        List<RiskAssessment> expected = new ArrayList<>(riskAssessments);

        when(riskAssessmentRepository.findByAssessmentNameContainsIgnoreCase(anyString())).thenReturn(riskAssessments);

        expected.sort(Comparator.comparing(Assessment::getDateCreated).reversed());

        List<RiskAssessment> actual = assessmentService.searchRiskAssessmentName("search term");

        assertEquals(expected, actual);
    }

    @Test
    void searchCoshhAssessmentAuthor() {
        List<CoshhAssessment> coshhAssessments = Collections.singletonList(coshhAssessment);

        List<CoshhAssessment> expected = new ArrayList<>(coshhAssessments);

        when(coshhAssessmentRepository.findByAuthor_DisplayNameContainsIgnoreCase(anyString())).thenReturn(coshhAssessments);

        expected.sort(Comparator.comparing(Assessment::getDateCreated).reversed());

        List<CoshhAssessment> actual = assessmentService.searchCoshhAssessmentAuthor("search term");

        assertEquals(expected, actual);
    }

    @Test
    void searchCoshhAssessmentName() {
        List<CoshhAssessment> coshhAssessments = Collections.singletonList(coshhAssessment);

        List<CoshhAssessment> expected = new ArrayList<>(coshhAssessments);

        when(coshhAssessmentRepository.findByAssessmentNameContainsIgnoreCase(anyString())).thenReturn(coshhAssessments);

        expected.sort(Comparator.comparing(Assessment::getDateCreated).reversed());

        List<CoshhAssessment> actual = assessmentService.searchCoshhAssessmentName("search term");

        assertEquals(expected, actual);
    }
}