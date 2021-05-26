package org.ordep.labtrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.model.BiologicalHazardCard;
import org.ordep.labtrack.model.ChemicalHazardCard;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.PhysicalHazardCard;
import org.ordep.labtrack.model.dto.RiskAssessmentDTO;
import org.ordep.labtrack.model.enums.PictogramType;
import org.ordep.labtrack.model.enums.SignalWord;
import org.ordep.labtrack.service.AssessmentService;
import org.ordep.labtrack.service.CardService;
import org.ordep.labtrack.service.StatementService;
import org.ordep.labtrack.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Controller
public class WebController {

    private final CardService cardService;
    private final StatementService statementService;
    private final UserService userService;
    private final AssessmentService assessmentService;

    public WebController(CardService cardService, StatementService statementService, UserService userService, AssessmentService assessmentService) {
        this.cardService = cardService;
        this.statementService = statementService;
        this.userService = userService;
        this.assessmentService = assessmentService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("allCards", cardService.getAllCards());
        model.addAttribute("title","Overview");
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title","Welcome");
        return "login";
    }

    @GetMapping("/card/{type}")
    public String chemical(@RequestParam UUID id, @PathVariable String type, Model model) {
        if (type.equals("ChemicalHazardCard")) {
            var chemicalHazardCard = cardService.findOneChemicalHazardCard(id);
            model.addAttribute("chemicalHazardCard", chemicalHazardCard);
            model.addAttribute("title",chemicalHazardCard.getCardName());
            return "cards/chemical";
        }
        if (type.equals("PhysicalHazardCard")) {
            var physicalHazardCard = cardService.findOnePhysicalHazardCard(id);
            model.addAttribute("physicalHazardCard", physicalHazardCard);
            model.addAttribute("title",physicalHazardCard.getCardName());
            return "cards/physical";
        }
        if (type.equals("BiologicalHazardCard")) {
            var biologicalHazardCard = cardService.findOneBiologicalHazardCard(id);
            model.addAttribute("biologicalHazardCard", cardService.findOneBiologicalHazardCard(id));
            model.addAttribute("title",biologicalHazardCard.getCardName());
            return "cards/biological";
        }

        return null;
    }

    @GetMapping("/cards")
    public String allCards(Model model) {
        model.addAttribute("allCards", cardService.getAllCards());
        model.addAttribute("title","All Cards");
        return "cards/allCards";
    }

    @GetMapping("/cards/chemical")
    public String chemicalCards(Model model) {
        LabTrackUser user = userService.getCurrentUser();
        model.addAttribute("chemicalCards", cardService.findAllChemicalHazardCardsForUser(user.getUserId()));
        model.addAttribute("title","Chemical Hazard Cards");
        return "cards/chemicals";
    }

    @GetMapping("/assessments")
    public String allAssessments(Model model) {
        model.addAttribute("allAssessments", assessmentService.getAllRiskAssessments());
        model.addAttribute("title","All Assessments");
        return "assessments/allAssessments";
    }

    @GetMapping("/assessments/risk")
    public String riskAssessments(Model model) {
        model.addAttribute("riskAssessments", assessmentService.getAllRiskAssessments());
        model.addAttribute("title","Risk Assessments");
        return "assessments/risk";
    }

    @GetMapping("/card/{type}/new")
    public String newCard(@PathVariable String type, Model model) {
        if (type.equals("ChemicalHazardCard")) {
            model.addAttribute("chemicalHazardCard", new ChemicalHazardCard());
            model.addAttribute("signalWords", SignalWord.values());
            model.addAttribute("hazardStatements", statementService.getAllHazardStatements());
            model.addAttribute("precautionaryStatements", statementService.getAllPrecautionaryStatements());
            model.addAttribute("pictogramTypes", PictogramType.values());
            model.addAttribute("title","New Chemical Hazard Card");
            return "cards/newChemical";
        }
        if (type.equals("PhysicalHazardCard")) {
            model.addAttribute("physicalHazardCard", new PhysicalHazardCard());
            model.addAttribute("title","New Physical Hazard Card");
            return "cards/newPhysical";
        }
        if (type.equals("BiologicalHazardCard")) {
            model.addAttribute("biologicalHazardCard", new BiologicalHazardCard());
            model.addAttribute("title","New Biological Hazard Card");
            return "cards/newBiological";
        }

        return null;
    }

    @PostMapping("/card/new/chemical")
    public String newCard(@ModelAttribute ChemicalHazardCard chemicalHazardCard, Model model) {
        log.info("New Chemical Hazard Card: {}", chemicalHazardCard);
        model.addAttribute(chemicalHazardCard);
        return "home";
    }

    @GetMapping("/assessment/risk/new")
    public String newRiskAssessment(Model model) {
        model.addAttribute("riskAssessment", new RiskAssessmentDTO());
        model.addAttribute("chemicalHazardCards", cardService.findAllChemicalHazardCards());
        model.addAttribute("biologicalHazardCards", cardService.findAllBiologicalHazardCards());
        model.addAttribute("physicalHazardCards", cardService.findAllPhysicalHazardCards());
        model.addAttribute("title","New Risk Assessment");
        return "assessments/newRiskAssessment";
    }
}
