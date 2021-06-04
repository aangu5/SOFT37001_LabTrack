package org.ordep.labtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.service.AssessmentService;
import org.ordep.labtrack.service.AuthenticationService;
import org.ordep.labtrack.service.CardService;
import org.ordep.labtrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = APIController.class)
class APIControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @MockBean
    private AssessmentService assessmentService;
    @MockBean
    private CardService cardService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createNewUser() throws Exception {

        mockMvc.perform(post("/api/register")
        .contentType("application/x-www-form-urlencoded;charset=UTF-8")
        .param("username", "user@email.com")
        .param("password-1", "Password1!")
        .param("password-2", "Password1!"))
                .andExpect(status().isCreated());

        verify(authenticationService, times(1)).saveAuthenticationEntity(any());
        verify(userService, times(1)).registerUser(any());

    }

    @Test
    void createNewUser_InvalidUsername() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("username", "user")
                .param("password-1", "Password1!")
                .param("password-2", "Password1!"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    void createNewUser_NullUsername() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("password-1", "Password1!")
                .param("password-2", "Password1!"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    void createNewUser_InvalidPassword() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("username", "user@email.com")
                .param("password-1", "pass")
                .param("password-2", "pass"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    void createNewUser_MismatchedPassword() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("username", "user@email.com")
                .param("password-1", "Password1!")
                .param("password-2", "Password2!"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    void createNewUser_NullPassword() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("username", "user@email.com"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    @WithMockUser(username = "user1", password = "pass", roles = "USER")
    void getRisks_ValidList() throws Exception {
        List<RiskAssessment> riskAssessmentList = Collections.singletonList(new RiskAssessment());
        when(assessmentService.getAllRiskAssessments()).thenReturn(riskAssessmentList);

        MvcResult result = mockMvc.perform(get("/api/risks"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedBody = objectMapper.writeValueAsString(riskAssessmentList);
        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).isEqualToIgnoringWhitespace(expectedBody);
    }

    @Test
    @WithMockUser(username = "user1", password = "pass", roles = "USER")
    void getRisks_EmptyList() throws Exception {
        List<RiskAssessment> riskAssessmentList = Collections.emptyList();
        when(assessmentService.getAllRiskAssessments()).thenReturn(riskAssessmentList);

        MvcResult result = mockMvc.perform(get("/api/risks"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedBody = objectMapper.writeValueAsString(riskAssessmentList);
        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).isEqualToIgnoringWhitespace(expectedBody);
    }

    @Test
    @WithMockUser(username = "user1", password = "pass", roles = "USER")
    void submitRiskAssessment() throws Exception {
        mockMvc.perform(post("/api/assessment/risk/new")
                .flashAttr("riskAssessment", new RiskAssessment()))
                .andExpect(status().isOk());

        verify(assessmentService, times(1)).newRiskAssessment(any());
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void changePassword() throws Exception {

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        AuthenticationEntity entity = objectMapper.readValue(new ClassPathResource(
                "/json/AuthenticationEntity.json").getInputStream(), AuthenticationEntity.class);

        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.getAuthenticationEntity(anyString())).thenReturn(entity);

        mockMvc.perform(post("/api/changePassword")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("old-password", "Password1!")
                .param("new-password-1", "Password2!")
                .param("new-password-2", "Password2!"))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).saveAuthenticationEntity(any());;
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void changePassword_NullOldPassword() throws Exception {

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        AuthenticationEntity entity = objectMapper.readValue(new ClassPathResource(
                "/json/AuthenticationEntity.json").getInputStream(), AuthenticationEntity.class);

        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.getAuthenticationEntity(anyString())).thenReturn(entity);

        mockMvc.perform(post("/api/changePassword")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("new-password-1", "Password2!")
                .param("new-password-2", "Password2!"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());;
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void changePassword_MismatchedNewPassword() throws Exception {

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        AuthenticationEntity entity = objectMapper.readValue(new ClassPathResource(
                "/json/AuthenticationEntity.json").getInputStream(), AuthenticationEntity.class);

        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.getAuthenticationEntity(anyString())).thenReturn(entity);

        mockMvc.perform(post("/api/changePassword")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("old-password", "Password1!")
                .param("new-password-1", "Password2!")
                .param("new-password-2", "Password3!"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());;
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void changePassword_InvalidNewPassword() throws Exception {

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        AuthenticationEntity entity = objectMapper.readValue(new ClassPathResource(
                "/json/AuthenticationEntity.json").getInputStream(), AuthenticationEntity.class);

        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.getAuthenticationEntity(anyString())).thenReturn(entity);

        mockMvc.perform(post("/api/changePassword")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("old-password", "Password1!")
                .param("new-password-1", "pass")
                .param("new-password-2", "pass"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());;
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void changePassword_WrongOldPassword() throws Exception {

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        AuthenticationEntity entity = objectMapper.readValue(new ClassPathResource(
                "/json/AuthenticationEntity.json").getInputStream(), AuthenticationEntity.class);

        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.getAuthenticationEntity(anyString())).thenReturn(entity);

        mockMvc.perform(post("/api/changePassword")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("old-password", "Password2!")
                .param("new-password-1", "Password2!")
                .param("new-password-2", "Password2!"))
                .andExpect(status().isFound());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());;
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void approveRiskAssessment() throws Exception {
        RiskAssessment riskAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(assessmentService.findOneRiskAssessment(any(UUID.class))).thenReturn(riskAssessment);
        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.canUserApprove(any())).thenReturn(true);

        mockMvc.perform(post("/api/assessment/risk/approve")
        .param("assessmentId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void approveRiskAssessment_UnableToApprove() throws Exception {
        RiskAssessment riskAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(assessmentService.findOneRiskAssessment(any(UUID.class))).thenReturn(riskAssessment);
        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.canUserApprove(any())).thenReturn(false);

        mockMvc.perform(post("/api/assessment/risk/approve")
                .param("assessmentId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user1", password = "pass", roles = "USER")
    void submitCoshhAssessment() throws Exception {
        mockMvc.perform(post("/api/assessment/coshh/new")
                .flashAttr("coshhAssessment", new CoshhAssessment()))
                .andExpect(status().isFound());

        verify(assessmentService, times(1)).newCoshhAssessment(any());
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void approveCoshhAssessment() throws Exception {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(assessmentService.findOneCoshhAssessment(any(UUID.class))).thenReturn(coshhAssessment);
        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.canUserApprove(any())).thenReturn(true);

        mockMvc.perform(post("/api/assessment/coshh/approve")
                .param("assessmentId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void approveCoshhAssessment_UnableToApprove() throws Exception {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(assessmentService.findOneCoshhAssessment(any(UUID.class))).thenReturn(coshhAssessment);
        when(userService.getCurrentUser()).thenReturn(user);
        when(authenticationService.canUserApprove(any())).thenReturn(false);

        mockMvc.perform(post("/api/assessment/coshh/approve")
                .param("assessmentId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void signCoshhAssessment() throws Exception {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(assessmentService.findOneCoshhAssessment(any(UUID.class))).thenReturn(coshhAssessment);
        when(userService.getCurrentUser()).thenReturn(user);
        when(assessmentService.canUserSignAssessment(any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/assessment/coshh/sign")
                .param("assessmentId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void signCoshhAssessment_UnableToSign() throws Exception {
        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        LabTrackUser user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        when(assessmentService.findOneCoshhAssessment(any(UUID.class))).thenReturn(coshhAssessment);
        when(userService.getCurrentUser()).thenReturn(user);
        when(assessmentService.canUserSignAssessment(any())).thenReturn(false);

        mockMvc.perform(post("/api/assessment/coshh/sign")
                .param("assessmentId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newChemicalHazardCard() throws Exception {
        mockMvc.perform(post("/api/card/chemical/new")
                .flashAttr("chemicalHazardCard", new ChemicalHazardCard()))
                .andExpect(status().isFound());

        verify(cardService, times(1)).newChemicalHazardCard(any());
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newBiologicalHazardCard() throws Exception {
        mockMvc.perform(post("/api/card/biological/new")
                .flashAttr("biologicalHazardCard", new BiologicalHazardCard()))
                .andExpect(status().isFound());

        verify(cardService, times(1)).newBiologicalHazardCard(any());
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newPhysicalHazardCard() throws Exception {
        mockMvc.perform(post("/api/card/physical/new")
                .flashAttr("physicalHazardCard", new PhysicalHazardCard()))
                .andExpect(status().isFound());

        verify(cardService, times(1)).newPhysicalHazardCard(any());
    }
}