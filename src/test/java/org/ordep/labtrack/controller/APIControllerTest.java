package org.ordep.labtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ordep.labtrack.model.RiskAssessment;
import org.ordep.labtrack.service.AssessmentService;
import org.ordep.labtrack.service.AuthenticationService;
import org.ordep.labtrack.service.CardService;
import org.ordep.labtrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

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
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AssessmentService assessmentService;
    @MockBean
    private CardService cardService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @Test
    void createNewUser() throws Exception {

        mockMvc.perform(post("/api/register")
        .contentType("application/x-www-form-urlencoded;charset=UTF-8")
        .param("username", "user@email.com")
        .param("password", "Password1!"))
                .andExpect(status().isMovedTemporarily());

    }

    @Test
    void createNewUser_InvalidUsername() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("username", "user")
                .param("password", "Password1!"))
                .andExpect(status().isMovedTemporarily());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    void createNewUser_NullUsername() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("password", "Password1!"))
                .andExpect(status().isMovedTemporarily());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    void createNewUser_InvalidPassword() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("username", "user@email.com")
                .param("password", "pass"))
                .andExpect(status().isMovedTemporarily());

        verify(authenticationService, times(0)).saveAuthenticationEntity(any());
        verify(userService, times(0)).registerUser(any());
    }

    @Test
    void createNewUser_NullPassword() throws Exception {

        mockMvc.perform(post("/api/register")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .param("username", "user@email.com"))
                .andExpect(status().isMovedTemporarily());

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
}