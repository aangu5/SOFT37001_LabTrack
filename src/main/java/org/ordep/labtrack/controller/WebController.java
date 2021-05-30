package org.ordep.labtrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.*;
import org.ordep.labtrack.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.ordep.labtrack.configuration.Constants.APPROVE;
import static org.ordep.labtrack.configuration.Constants.PAGE_TITLE;

@Slf4j
@Controller
public class WebController {

    private final CardService cardService;
    private final StatementService statementService;
    private final UserService userService;
    private final AssessmentService assessmentService;
    private final AuthenticationService authenticationService;

    public WebController(CardService cardService, StatementService statementService, UserService userService, AssessmentService assessmentService, AuthenticationService authenticationService) {
        this.cardService = cardService;
        this.statementService = statementService;
        this.userService = userService;
        this.assessmentService = assessmentService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("allCards", cardService.getAllCards());
        model.addAttribute("allAssessments", assessmentService.getAllRiskAssessments());
        model.addAttribute("allCoshhAssessments", assessmentService.getAllCoshhAssessments());
        model.addAttribute(PAGE_TITLE,"Overview");
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute(PAGE_TITLE,"My Profile");
        model.addAttribute("user",userService.getCurrentUser());

        return "profile";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute(PAGE_TITLE,"Welcome");
        return "login";
    }

    @GetMapping("/login/forgotten")
    public String reset(Model model) {
        model.addAttribute(PAGE_TITLE,"Reset Your Password");
        return "forgotten";
    }

    @GetMapping("/card/{type}")
    public String chemical(@RequestParam UUID id, @PathVariable String type, Model model) {
        if (type.equals("ChemicalHazardCard")) {
            var chemicalHazardCard = cardService.findOneChemicalHazardCard(id);
            model.addAttribute("chemicalHazardCard", chemicalHazardCard);
            model.addAttribute(PAGE_TITLE,chemicalHazardCard.getCardName());
            return "cards/chemical";
        }
        if (type.equals("PhysicalHazardCard")) {
            var physicalHazardCard = cardService.findOnePhysicalHazardCard(id);
            model.addAttribute("physicalHazardCard", physicalHazardCard);
            model.addAttribute(PAGE_TITLE,physicalHazardCard.getCardName());
            return "cards/physical";
        }
        if (type.equals("BiologicalHazardCard")) {
            var biologicalHazardCard = cardService.findOneBiologicalHazardCard(id);
            model.addAttribute("biologicalHazardCard", cardService.findOneBiologicalHazardCard(id));
            model.addAttribute(PAGE_TITLE,biologicalHazardCard.getCardName());
            return "cards/biological";
        }

        return null;
    }

    @GetMapping("/cards")
    public String allCards(Model model) {
        model.addAttribute("allCards", cardService.getAllCards());
        model.addAttribute(PAGE_TITLE,"All Cards");
        return "cards/allCards";
    }

    @GetMapping("/cards/chemical")
    public String chemicalCards(@RequestParam(defaultValue = "none") String view,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        List<ChemicalHazardCard> cards;

        if (view.equals("self")) {
            LabTrackUser user = userService.getCurrentUser();
            cards = cardService.findAllChemicalHazardCardsForUser(user.getUserId(), page);
        } else if (view.equals(APPROVE)) {
            cards = new ArrayList<>();
        } else {
            cards = cardService.findAllChemicalHazardCards();
        }

        model.addAttribute("chemicalCards", cards);
        model.addAttribute(PAGE_TITLE,"Chemical Hazard Cards");
        return "cards/chemicals";
    }

    @GetMapping("/cards/biological")
    public String biologicalCards(@RequestParam(defaultValue = "none") String view, Model model) {
        List<BiologicalHazardCard> cards;

        if (view.equals("self")) {
            LabTrackUser user = userService.getCurrentUser();
            cards = cardService.findAllBiologicalHazardCardsForUser(user.getUserId());
        } else if (view.equals(APPROVE)) {
            cards = new ArrayList<>();
        } else {
            cards = cardService.findAllBiologicalHazardCards();
        }

        model.addAttribute("biologicalCards", cards);
        model.addAttribute(PAGE_TITLE,"Biological Hazard Cards");
        return "cards/biologicals";
    }

    @GetMapping("/cards/physical")
    public String physicalCards(@RequestParam(defaultValue = "none") String view, Model model) {
        List<PhysicalHazardCard> cards;

        if (view.equals("self")) {
            LabTrackUser user = userService.getCurrentUser();
            cards = cardService.findAllPhysicalHazardCardsForUser(user.getUserId());
        } else {
            cards = cardService.findAllPhysicalHazardCards();
        }

        model.addAttribute("physicalCards", cards);
        model.addAttribute(PAGE_TITLE,"Physical Hazard Cards");
        return "cards/physicals";
    }

    @GetMapping("/assessments")
    public String allAssessments(Model model) {
        model.addAttribute("allAssessments", assessmentService.getAllRiskAssessments());
        model.addAttribute(PAGE_TITLE,"All Assessments");
        return "assessments/allAssessments";
    }

    @GetMapping("/assessments/risk")
    public String riskAssessments(@RequestParam(defaultValue = "none") String view, Model model) {
        List<RiskAssessment> assessments;

        if (view.equals("self")) {
            LabTrackUser user = userService.getCurrentUser();
            assessments = assessmentService.findAllRiskAssessmentsForUser(user.getUserId());
        } else if (view.equals("approve")) {
            assessments = assessmentService.findAllRiskAssessmentsToApprove();
        } else {
            assessments = assessmentService.getAllRiskAssessments();
        }

        model.addAttribute("riskAssessments", assessments);
        model.addAttribute(PAGE_TITLE,"Risk Assessments");
        return "assessments/riskAssessments";
    }

    @GetMapping("/card/{type}/new")
    public String newCard(@PathVariable String type, Model model) {
        if (type.equalsIgnoreCase("ChemicalHazardCard")) {
            model.addAttribute("chemicalHazardCard", new ChemicalHazardCard());
            model.addAttribute("signalWords", SignalWord.values());
            model.addAttribute("hazardStatements", statementService.getAllHazardStatements());
            model.addAttribute("precautionaryStatements", statementService.getAllPrecautionaryStatements());
            model.addAttribute("pictogramTypes", ChemicalPictogram.values());
            model.addAttribute(PAGE_TITLE,"New Chemical Hazard Card");
            return "cards/newChemical";
        }
        if (type.equalsIgnoreCase("PhysicalHazardCard")) {
            model.addAttribute("physicalHazardCard", new PhysicalHazardCard());
            model.addAttribute("pictogramTypes", PhysicalPictogram.values());
            model.addAttribute(PAGE_TITLE,"New Physical Hazard Card");
            return "cards/newPhysical";
        }
        if (type.equalsIgnoreCase("BiologicalHazardCard")) {
            model.addAttribute("biologicalHazardCard", new BiologicalHazardCard());
            model.addAttribute(PAGE_TITLE,"New Biological Hazard Card");
            return "cards/newBiological";
        }

        return null;
    }

    @GetMapping("/assessment/risk/new")
    public String newRiskAssessment(Model model) {
        model.addAttribute("riskAssessment", new RiskAssessment());
        model.addAttribute("chemicalHazardCards", cardService.findAllChemicalHazardCards());
        model.addAttribute("biologicalHazardCards", cardService.findAllBiologicalHazardCards());
        model.addAttribute("physicalHazardCards", cardService.findAllPhysicalHazardCards());
        model.addAttribute("frequencies", FrequencyOfTask.values());
        model.addAttribute("severities", Severity.values());
        model.addAttribute("likelihoods", Likelihood.values());
        model.addAttribute(PAGE_TITLE,"New Risk Assessment");
        return "assessments/newRiskAssessment";
    }

    @GetMapping("/assessments/coshh")
    public String coshhAssessments(@RequestParam(defaultValue = "none") String view, Model model) {
        List<CoshhAssessment> assessments;

        if (view.equals("self")) {
            LabTrackUser user = userService.getCurrentUser();
            assessments = assessmentService.findAllCoshhAssessmentsForUser(user.getUserId());
        } else if (view.equals("approve")){
            assessments = assessmentService.findAllCoshhAssessmentsToApprove();
        } else {
            assessments = assessmentService.getAllCoshhAssessments();
        }
        model.addAttribute("coshhAssessments", assessments);
        model.addAttribute(PAGE_TITLE, "COSHH Assessments");

        return "assessments/coshhAssessments";
    }

    @GetMapping("/assessment/coshh/new")
    public String newCoshhAssessment(Model model) {
        model.addAttribute("coshhAssessment", new CoshhAssessment());
        model.addAttribute("precautionaryStatements", statementService.getAllPrecautionaryStatements());
        model.addAttribute("hazardStatements", statementService.getAllHazardStatements());
        model.addAttribute("hazardToHealths", HazardToHealth.values());
        model.addAttribute("routeOfExposures", RouteOfExposure.values());
        model.addAttribute("precautions", Precaution.values());
        model.addAttribute("protectiveEquipments", ProtectiveEquipment.values());

        model.addAttribute(PAGE_TITLE,"New COSHH Assessment");
        return "assessments/newCoshhAssessment";
    }

    @GetMapping("/assessment/risk")
    public String riskAssessment(@RequestParam UUID id, Model model) {
        LabTrackUser user = userService.getCurrentUser();
        var riskAssessment = assessmentService.findOneRiskAssessment(id);
        model.addAttribute("riskAssessment", riskAssessment);
        model.addAttribute("canUserApprove", authenticationService.canUserApprove(user));
        model.addAttribute(PAGE_TITLE,riskAssessment.getAssessmentName());
        return "assessments/riskAssessment";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute(PAGE_TITLE,"Error");
        return "error";
    }
}
