package org.ordep.labtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ordep.labtrack.model.*;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private ChemicalHazardCard chemicalHazardCard;
    private BiologicalHazardCard biologicalHazardCard;
    private PhysicalHazardCard physicalHazardCard;

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

        chemicalHazardCard = objectMapper.readValue(new ClassPathResource(
                "/json/ChemicalHazardCard.json").getInputStream(), ChemicalHazardCard.class);

        biologicalHazardCard = objectMapper.readValue(new ClassPathResource(
                "/json/BiologicalHazardCard.json").getInputStream(), BiologicalHazardCard.class);

        physicalHazardCard = objectMapper.readValue(new ClassPathResource(
                "/json/PhysicalHazardCard.json").getInputStream(), PhysicalHazardCard.class);
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
        when(cardService.findOneChemicalHazardCard(any(UUID.class))).thenReturn(chemicalHazardCard);

        mockMvc.perform(get("/card/chemical")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void biological() throws Exception {
        when(cardService.findOneBiologicalHazardCard(any(UUID.class))).thenReturn(biologicalHazardCard);

        mockMvc.perform(get("/card/biological")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void physical() throws Exception {
        when(cardService.findOnePhysicalHazardCard(any(UUID.class))).thenReturn(physicalHazardCard);

        mockMvc.perform(get("/card/physical")
                .param("id", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void allCards() throws Exception {
        List<Card> cards = Arrays.asList(chemicalHazardCard, physicalHazardCard, biologicalHazardCard);
        when(cardService.getAllCards()).thenReturn(cards);

        mockMvc.perform(get("/cards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("allCards", equalTo(cards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void allCards_SearchName() throws Exception {
        List<Card> cards = Arrays.asList(chemicalHazardCard, physicalHazardCard, biologicalHazardCard);
        when(cardService.searchCardName(anyString())).thenReturn(cards);

        mockMvc.perform(get("/cards?name=something"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("allCards", equalTo(cards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void allCards_SearchAuthor() throws Exception {
        List<Card> cards = Arrays.asList(chemicalHazardCard, physicalHazardCard, biologicalHazardCard);
        when(cardService.searchCardAuthor(anyString())).thenReturn(cards);

        mockMvc.perform(get("/cards?author=someone"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("allCards", equalTo(cards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void chemicalCards() throws Exception {
        List<ChemicalHazardCard> chemicalHazardCards = Collections.singletonList(chemicalHazardCard);
        when(cardService.findAllChemicalHazardCards()).thenReturn(chemicalHazardCards);

        mockMvc.perform(get("/cards/chemical"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("chemicalCards", equalTo(chemicalHazardCards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void chemicalCards_OwnCards() throws Exception {
        List<ChemicalHazardCard> chemicalHazardCards = Collections.singletonList(chemicalHazardCard);
        when(cardService.findAllChemicalHazardCardsForUser(any(UUID.class), anyInt())).thenReturn(chemicalHazardCards);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/cards/chemical?view=self"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("chemicalCards", equalTo(chemicalHazardCards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void biologicalCards() throws Exception {
        List<BiologicalHazardCard> biologicalHazardCards = Collections.singletonList(biologicalHazardCard);
        when(cardService.findAllBiologicalHazardCards()).thenReturn(biologicalHazardCards);

        mockMvc.perform(get("/cards/biological"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("biologicalCards", equalTo(biologicalHazardCards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void biologicalCards_OwnCards() throws Exception {
        List<BiologicalHazardCard> biologicalHazardCards = Collections.singletonList(biologicalHazardCard);
        when(cardService.findAllBiologicalHazardCardsForUser(any(UUID.class))).thenReturn(biologicalHazardCards);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/cards/biological?view=self"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("biologicalCards", equalTo(biologicalHazardCards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void physicalCards() throws Exception {
        List<PhysicalHazardCard> physicalHazardCards = Collections.singletonList(physicalHazardCard);
        when(cardService.findAllPhysicalHazardCards()).thenReturn(physicalHazardCards);

        mockMvc.perform(get("/cards/physical"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("physicalCards", equalTo(physicalHazardCards)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void physicalCards_OwnCards() throws Exception {
        List<PhysicalHazardCard> physicalHazardCards = Collections.singletonList(physicalHazardCard);
        when(cardService.findAllPhysicalHazardCardsForUser(any(UUID.class))).thenReturn(physicalHazardCards);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/cards/physical?view=self"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("physicalCards", equalTo(physicalHazardCards)));
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
    void riskAssessments_OwnAssessments() throws Exception {
        List<RiskAssessment> assessments = Collections.singletonList(riskAssessment);
        when(assessmentService.findAllRiskAssessmentsForUser(any(UUID.class))).thenReturn(assessments);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/assessments/risk?view=self"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("riskAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void riskAssessments_ToApprove() throws Exception {
        List<RiskAssessment> assessments = Collections.singletonList(riskAssessment);
        when(assessmentService.findAllRiskAssessmentsToApprove()).thenReturn(assessments);

        mockMvc.perform(get("/assessments/risk?view=approve"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("riskAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void riskAssessments_SearchName() throws Exception {
        List<RiskAssessment> assessments = Collections.singletonList(riskAssessment);
        when(assessmentService.searchRiskAssessmentName(anyString())).thenReturn(assessments);

        mockMvc.perform(get("/assessments/risk?view=search&name=something"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("riskAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void riskAssessments_SearchAuthor() throws Exception {
        List<RiskAssessment> assessments = Collections.singletonList(riskAssessment);
        when(assessmentService.searchRiskAssessmentAuthor(anyString())).thenReturn(assessments);

        mockMvc.perform(get("/assessments/risk?view=search&author=something"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("riskAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void riskAssessments_SearchBoth() throws Exception {
        List<RiskAssessment> assessments = Collections.singletonList(riskAssessment);
        when(assessmentService.getAllRiskAssessments()).thenReturn(assessments);

        mockMvc.perform(get("/assessments/risk?view=search&name=something&author=somethingElse"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("riskAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newChemicalHazardCard() throws Exception {
        mockMvc.perform(get("/card/chemical/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("chemicalHazardCard", equalTo(new ChemicalHazardCard())));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newBiologicalHazardCard() throws Exception {
        mockMvc.perform(get("/card/biological/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("biologicalHazardCard", equalTo(new BiologicalHazardCard())));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void newPhysicalHazardCard() throws Exception {
        mockMvc.perform(get("/card/physical/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("physicalHazardCard", equalTo(new PhysicalHazardCard())));
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
    void coshhAssessments_OwnAssessments() throws Exception {
        List<CoshhAssessment> assessments = Collections.singletonList(coshhAssessment);
        when(assessmentService.findAllCoshhAssessmentsForUser(any(UUID.class))).thenReturn(assessments);
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/assessments/coshh?view=self"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("coshhAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void coshhAssessments_ToApprove() throws Exception {
        List<CoshhAssessment> assessments = Collections.singletonList(coshhAssessment);
        when(assessmentService.findAllCoshhAssessmentsToApprove()).thenReturn(assessments);

        mockMvc.perform(get("/assessments/coshh?view=approve"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("coshhAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void coshhAssessments_SearchName() throws Exception {
        List<CoshhAssessment> assessments = Collections.singletonList(coshhAssessment);
        when(assessmentService.searchCoshhAssessmentName(anyString())).thenReturn(assessments);

        mockMvc.perform(get("/assessments/coshh?view=search&name=something"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("coshhAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void coshhAssessments_SearchAuthor() throws Exception {
        List<CoshhAssessment> assessments = Collections.singletonList(coshhAssessment);
        when(assessmentService.searchCoshhAssessmentAuthor(anyString())).thenReturn(assessments);

        mockMvc.perform(get("/assessments/coshh?view=search&author=something"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("coshhAssessments", equalTo(assessments)));
    }

    @Test
    @WithMockUser(username = "user1", password = "Password1!", roles = "USER")
    void coshhAssessments_SearchBoth() throws Exception {
        List<CoshhAssessment> assessments = Collections.singletonList(coshhAssessment);
        when(assessmentService.getAllCoshhAssessments()).thenReturn(assessments);

        mockMvc.perform(get("/assessments/coshh?view=search&name=something&author=somethingElse"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("coshhAssessments", equalTo(assessments)));
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