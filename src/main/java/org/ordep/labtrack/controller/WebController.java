package org.ordep.labtrack.controller;

import org.ordep.labtrack.model.BiologicalHazardCard;
import org.ordep.labtrack.model.ChemicalHazardCard;
import org.ordep.labtrack.model.PhysicalHazardCard;
import org.ordep.labtrack.model.RiskAssessment;
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
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/card/{type}")
    public String chemical(@RequestParam UUID id, @PathVariable String type, Model model) {
        if (type.equals("ChemicalHazardCard")) {
            model.addAttribute("chemicalHazardCard", cardService.findOneChemicalHazardCard(id));
            return "cards/chemical";
        }
        if (type.equals("PhysicalHazardCard")) {
            model.addAttribute("physicalHazardCard", cardService.findOnePhysicalHazardCard(id));
            return "cards/physical";
        }
        if (type.equals("BiologicalHazardCard")) {
            model.addAttribute("biologicalHazardCard", cardService.findOneBiologicalHazardCard(id));
            return "cards/biological";
        }

        return null;
    }

    @GetMapping("/cards")
    public String allCards(Model model) {
        model.addAttribute("allCards", cardService.getAllCards());
        return "cards/allCards";
    }

    @GetMapping("/cards/chemical")
    public String chemicalCards(Model model) {
        model.addAttribute("chemicalCards", cardService.findAllChemicalHazardCardsForUser(userService.getCurrentUser()));
        return "cards/chemicals";
    }

    @GetMapping("/assessments")
    public String allAssessments(Model model) {
        model.addAttribute("allAssessments", assessmentService.getAllRiskAssessments());
        return "assessments/allAssessments";
    }

    @GetMapping("/assessments/risk")
    public String riskAssessments(Model model) {
        model.addAttribute("riskAssessments", assessmentService.getAllRiskAssessments());
        return "assessments/risk";
    }

    @GetMapping("/card/{type}/new")
    public String newCard(@PathVariable String type, Model model) {
        if (type.equals("ChemicalHazardCard")) {
            model.addAttribute("chemicalHazardCard", new ChemicalHazardCard());
            model.addAttribute("signalWords", SignalWord.values());
            model.addAttribute("hazardStatements", statementService.getAllHazardStatements());
            model.addAttribute("precautionaryStatements", statementService.getAllPrecautionaryStatements());
            model.addAttribute("pictograms", PictogramType.values());
            return "cards/newChemical";
        }
        if (type.equals("PhysicalHazardCard")) {
            model.addAttribute("physicalHazardCard", new PhysicalHazardCard());
            return "cards/newPhysical";
        }
        if (type.equals("BiologicalHazardCard")) {
            model.addAttribute("biologicalHazardCard", new BiologicalHazardCard());
            return "cards/newBiological";
        }

        return null;
    }

    @PostMapping("/card/new/chemical")
    public String newCard(@ModelAttribute ChemicalHazardCard chemicalHazardCard, Model model) {
        System.out.println(chemicalHazardCard);
        model.addAttribute(chemicalHazardCard);
        return "home";
    }

    @GetMapping("/assessment/risk/new")
    public String newRiskAssessment(Model model) {
        model.addAttribute("riskAssessment", new RiskAssessmentDTO());
        model.addAttribute("chemicalHazardCards", cardService.findAllChemicalHazardCards());
        model.addAttribute("biologicalHazardCards", cardService.findAllBiologicalHazardCards());
        model.addAttribute("physicalHazardCards", cardService.findAllPhysicalHazardCards());
        return "assessments/newRiskAssessment";
    }
}
