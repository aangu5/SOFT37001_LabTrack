package org.ordep.labtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.CoshhAssessment;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.RiskAssessment;
import org.ordep.labtrack.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService cardService;
    @MockBean
    private StatementService statementService;
    @MockBean
    private UserService userService;
    @MockBean
    private AssessmentService assessmentService;
    @MockBean
    private AuthenticationService authenticationService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private CoshhAssessment coshhAssessment;
    private LabTrackUser user;
    private RiskAssessment riskAssessment;
    private AuthenticationEntity authenticationEntity;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());

        user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

        authenticationEntity = objectMapper.readValue(new ClassPathResource(
                "/json/AuthenticationEntity.json").getInputStream(), AuthenticationEntity.class);

        riskAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/RiskAssessment.json").getInputStream(), RiskAssessment.class);

        coshhAssessment = objectMapper.readValue(new ClassPathResource(
                "/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void home() throws Exception {
        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void profile() throws Exception {
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void changePassword() throws Exception {
        mockMvc.perform(get("/profile/change-password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void manageUsers() throws Exception {
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void viewUser() throws Exception {
        when(userService.findUser(any())).thenReturn(user);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/user")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void reset() throws Exception {
        mockMvc.perform(get("/login/forgotten"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void chemical() throws Exception {
        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void allCards() throws Exception {
        mockMvc.perform(get("/cards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void chemicalCards() throws Exception {
        mockMvc.perform(get("/cards/chemical"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void biologicalCards() throws Exception {
        mockMvc.perform(get("/cards/biological"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void physicalCards() throws Exception {
        mockMvc.perform(get("/cards/physical"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void allAssessments() throws Exception {
        mockMvc.perform(get("/assessments"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void riskAssessments() throws Exception {
        mockMvc.perform(get("/assessments/risk"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newCard() throws Exception {
        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newRiskAssessment() throws Exception {
        mockMvc.perform(get("/assessment/risk/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void coshhAssessments() throws Exception {
        mockMvc.perform(get("/assessments/coshh"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void coshhAssessment() throws Exception {
        when(assessmentService.findOneCoshhAssessment(any())).thenReturn(coshhAssessment);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/assessment/coshh")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newCoshhAssessment() throws Exception {
        mockMvc.perform(get("/assessment/coshh/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void copyCoshhAssessment() throws Exception {
        when(assessmentService.findOneCoshhAssessment(any())).thenReturn(coshhAssessment);

        mockMvc.perform(get("/assessment/coshh/copy")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void deleteCoshhAssessment() throws Exception {
        when(assessmentService.findOneCoshhAssessment(any())).thenReturn(coshhAssessment);

        mockMvc.perform(get("/assessment/coshh/delete")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void riskAssessment() throws Exception {
        when(assessmentService.findOneRiskAssessment(any())).thenReturn(riskAssessment);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/assessment/risk")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void copyRiskAssessment() throws Exception {
        when(assessmentService.findOneRiskAssessment(any())).thenReturn(riskAssessment);

        mockMvc.perform(get("/assessment/risk/copy")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void deleteRiskAssessment() throws Exception {
        when(assessmentService.findOneRiskAssessment(any())).thenReturn(riskAssessment);
        mockMvc.perform(get("/assessment/risk/delete")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void error() throws Exception {
        mockMvc.perform(get("/error"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}