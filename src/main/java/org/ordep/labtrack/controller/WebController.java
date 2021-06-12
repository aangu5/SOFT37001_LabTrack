package org.ordep.labtrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.configuration.Constants;
import org.ordep.labtrack.configuration.LabTrackUtilities;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.*;
import org.ordep.labtrack.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.ordep.labtrack.configuration.Constants.*;

@SuppressWarnings("SameReturnValue")
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
        model.addAttribute("allAssessments", assessmentService.getAllRiskAssessments());
        model.addAttribute("allCoshhAssessments", assessmentService.getAllCoshhAssessments());
        model.addAttribute(PAGE_TITLE,"Overview");
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute(PAGE_TITLE,"My Profile");
        LabTrackUser currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("role", LabTrackUtilities.getHighestRole(currentUser));
        model.addAttribute("permitted", LabTrackUtilities.canUserApprove(currentUser));
        return "profile";
    }

    @GetMapping("/profile/change-password")
    public String changePassword(Model model) {
        model.addAttribute("passwordRegex", Constants.PASSWORD_REGEX);
        model.addAttribute(PAGE_TITLE,"Change Password");
        return "changePassword";
    }

    @GetMapping("/users")
    public String manageUsers(Model model){
        model.addAttribute(PAGE_TITLE,"View Users");
        model.addAttribute("permitted", LabTrackUtilities.canUserApprove(userService.getCurrentUser()));
        List<LabTrackUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/user")
    public String viewUser(@RequestParam UUID id, Model model){
        var user = userService.findUser(id);
        var userRole = userService.getCurrentUser().getRoles().get(0);
        model.addAttribute("senior",LabTrackUtilities.isUserSenior(user.getRoles().get(0),userRole));
        model.addAttribute("user", user);
        model.addAttribute("role", userRole);
        model.addAttribute("roles", LabTrackUtilities.getJuniorRoles(userRole));
        model.addAttribute(PAGE_TITLE,"View User: " + user.getDisplayName());
        return "user";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("emailRegex", Constants.EMAIL_REGEX);
        model.addAttribute("passwordRegex", Constants.PASSWORD_REGEX);
        model.addAttribute(PAGE_TITLE,"Welcome");

        return "login";
    }

    @GetMapping("/login/forgotten")
    public String reset(Model model) {
        model.addAttribute(PAGE_TITLE,"Reset Your Password");
        return "forgotten";
    }

    @GetMapping("/card/chemical")
    public String chemical(@RequestParam UUID id, Model model) {
        var chemicalHazardCard = cardService.findOneChemicalHazardCard(id);
        model.addAttribute("chemicalHazardCard", chemicalHazardCard);
        model.addAttribute(PAGE_TITLE,chemicalHazardCard.getCardName());
        return "cards/chemical";
    }

    @GetMapping("/card/biological")
    public String biological(@RequestParam UUID id, Model model) {
        var biologicalHazardCard = cardService.findOneBiologicalHazardCard(id);
        model.addAttribute("biologicalHazardCard", biologicalHazardCard);
        model.addAttribute(PAGE_TITLE,biologicalHazardCard.getCardName());
        return "cards/biological";
    }

    @GetMapping("/card/physical")
    public String physical(@RequestParam UUID id, Model model) {
        var physicalHazardCard = cardService.findOnePhysicalHazardCard(id);
        model.addAttribute("physicalHazardCard", physicalHazardCard);
        model.addAttribute(PAGE_TITLE,physicalHazardCard.getCardName());
        return "cards/physical";
    }

    @GetMapping("/cards")
    public String allCards(@RequestParam(defaultValue = NONE) String name, @RequestParam(defaultValue = NONE) String author, Model model) {
        List<Card> queryResult;

        if (author.equals(NONE) && !name.equals(NONE)) {
            queryResult = cardService.searchCardName(name);
            model.addAttribute(NAME_SEARCH, name);
        } else if (!author.equals(NONE) && name.equals(NONE)){
            queryResult = cardService.searchCardAuthor(author);
            model.addAttribute(AUTHOR_SEARCH, author);
        } else {
            queryResult = cardService.getAllCards();
        }

        model.addAttribute("allCards", queryResult);
        model.addAttribute(PAGE_TITLE,"All Cards");
        return "cards/allCards";
    }

    @GetMapping("/cards/chemical")
    public String chemicalCards(@RequestParam(defaultValue = NONE) String view,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        List<ChemicalHazardCard> cards;

        if (view.equals("self")) {
            LabTrackUser user = userService.getCurrentUser();
            cards = cardService.findAllChemicalHazardCardsForUser(user.getUserId(), page);
        }  else {
            cards = cardService.findAllChemicalHazardCards();
        }

        model.addAttribute("chemicalCards", cards);
        model.addAttribute(PAGE_TITLE,"Chemical Hazard Cards");
        return "cards/chemicals";
    }

    @GetMapping("/cards/biological")
    public String biologicalCards(@RequestParam(defaultValue = NONE) String view, Model model) {
        List<BiologicalHazardCard> cards;

        if (view.equals("self")) {
            LabTrackUser user = userService.getCurrentUser();
            cards = cardService.findAllBiologicalHazardCardsForUser(user.getUserId());
        } else {
            cards = cardService.findAllBiologicalHazardCards();
        }

        model.addAttribute("biologicalCards", cards);
        model.addAttribute(PAGE_TITLE,"Biological Hazard Cards");
        return "cards/biologicals";
    }

    @GetMapping("/cards/physical")
    public String physicalCards(@RequestParam(defaultValue = NONE) String view, Model model) {
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
        model.addAttribute("allAssessments", assessmentService.getAllAssessments());
        model.addAttribute(PAGE_TITLE,"All Assessments");
        return "assessments/allAssessments";
    }

    @GetMapping("/assessments/risk")
    public String riskAssessments(@RequestParam(defaultValue = NONE) String view,
                                  @RequestParam(defaultValue = NONE) String name,
                                  @RequestParam(defaultValue = NONE) String author, Model model) {
        List<RiskAssessment> assessments;

        switch (view) {
            case "self":
                LabTrackUser user = userService.getCurrentUser();
                assessments = assessmentService.findAllRiskAssessmentsForUser(user.getUserId());
                break;
            case "approve":
                assessments = assessmentService.findAllRiskAssessmentsToApprove();
                break;
            case "search":
                if (author.equals(NONE) && !name.equals(NONE)) {
                    assessments = assessmentService.searchRiskAssessmentName(name);
                    model.addAttribute(NAME_SEARCH, name);
                } else if (!author.equals(NONE) && name.equals(NONE)){
                    assessments = assessmentService.searchRiskAssessmentAuthor(author);
                    model.addAttribute(AUTHOR_SEARCH, author);
                } else {
                    assessments = assessmentService.getAllRiskAssessments();
                }
                break;
            default:
                assessments = assessmentService.getAllRiskAssessments();
                break;
        }

        model.addAttribute("riskAssessments", assessments);
        model.addAttribute(PAGE_TITLE,"Risk Assessments");
        return "assessments/riskAssessments";
    }

    @GetMapping("/card/chemical/new")
    public String newChemicalHazardCard(Model model) {
        model.addAttribute("chemicalHazardCard", new ChemicalHazardCard());
        model.addAttribute("signalWords", SignalWord.values());
        model.addAttribute(HAZARD_STATEMENTS, statementService.getAllHazardStatements());
        model.addAttribute(PRECAUTIONARY_STATEMENTS, statementService.getAllPrecautionaryStatements());
        model.addAttribute("pictogramTypes", ChemicalPictogram.values());
        model.addAttribute(PAGE_TITLE,"New Chemical Hazard Card");
        return "cards/newChemical";
    }

    @GetMapping("/card/biological/new")
    public String newBiologicalHazardCard(Model model) {
        model.addAttribute("biologicalHazardCard", new BiologicalHazardCard());
        model.addAttribute("bioSafetyLevel", BioSafetyLevel.values());
        model.addAttribute(PAGE_TITLE,"New Biological Hazard Card");
        return "cards/newBiological";
    }

    @GetMapping("/card/physical/new")
    public String newPhysicalHazardCard(Model model) {
        model.addAttribute("physicalHazardCard", new PhysicalHazardCard());
        model.addAttribute("pictogramTypes", PhysicalPictogram.values());
        model.addAttribute("mandatoryPictograms", MandatoryPictogram.values());
        model.addAttribute(PAGE_TITLE,"New Physical Hazard Card");
        return "cards/newPhysical";
    }

    @GetMapping("/assessment/risk/new")
    public String newRiskAssessment(Model model) {
        model.addAttribute(RISK_ASSESSMENT, new RiskAssessment());
        setRiskAssessmentFields(model);
        model.addAttribute(PAGE_TITLE,"New Risk Assessment");
        return "assessments/newRiskAssessment";
    }

    @GetMapping("/assessments/coshh")
    public String coshhAssessments(@RequestParam(defaultValue = NONE) String view,
                                   @RequestParam(defaultValue = NONE) String name,
                                   @RequestParam(defaultValue = NONE) String author, Model model) {
        List<CoshhAssessment> assessments;

        switch (view) {
            case "self":
                LabTrackUser user = userService.getCurrentUser();
                assessments = assessmentService.findAllCoshhAssessmentsForUser(user.getUserId());
                break;
            case "approve":
                assessments = assessmentService.findAllCoshhAssessmentsToApprove();
                break;
            case "search":
                if (author.equals(NONE) && !name.equals(NONE)) {
                    assessments = assessmentService.searchCoshhAssessmentName(name);
                    model.addAttribute(NAME_SEARCH, name);
                } else if (!author.equals(NONE) && name.equals(NONE)) {
                    assessments = assessmentService.searchCoshhAssessmentAuthor(author);
                    model.addAttribute(AUTHOR_SEARCH, author);
                } else {
                    assessments = assessmentService.getAllCoshhAssessments();
                }
                break;
            default:
                assessments = assessmentService.getAllCoshhAssessments();
                break;
        }
        model.addAttribute("coshhAssessments", assessments);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute(PAGE_TITLE, "COSHH Assessments");

        return "assessments/coshhAssessments";
    }

    @GetMapping("/assessment/coshh")
    public String coshhAssessment(@RequestParam(defaultValue = NONE) UUID id, Model model) {
        LabTrackUser user = userService.getCurrentUser();

        var assessment = assessmentService.findOneCoshhAssessment(id);
        model.addAttribute(COSHH_ASSESSMENT, assessment);
        model.addAttribute("canUserApprove", LabTrackUtilities.canUserApprove(user));
        model.addAttribute("canUserSign", assessmentService.canUserSignAssessment(id));
        model.addAttribute("isUserOwner", assessment.getAuthor() == user);
        model.addAttribute("approved", assessment.getStatus() != AssessmentState.PENDING);

        model.addAttribute(PAGE_TITLE, assessment.getAssessmentName());

        return "assessments/coshhAssessment";
    }

    @GetMapping("/assessment/coshh/new")
    public String newCoshhAssessment(Model model) {
        model.addAttribute(COSHH_ASSESSMENT, new CoshhAssessment());
        setCoshhAssessmentFields(model);

        model.addAttribute(PAGE_TITLE,"New COSHH Assessment");
        return "assessments/newCoshhAssessment";
    }

    private void setCoshhAssessmentFields(Model model) {
        model.addAttribute(PRECAUTIONARY_STATEMENTS, statementService.getAllPrecautionaryStatements());
        model.addAttribute(HAZARD_STATEMENTS, statementService.getAllHazardStatements());
        model.addAttribute("hazardToHealths", HazardToHealth.values());
        model.addAttribute("routeOfExposures", RouteOfExposure.values());
        model.addAttribute("precautions", Precaution.values());
        model.addAttribute("protectiveEquipments", ProtectiveEquipment.values());
    }

    @GetMapping("/assessment/coshh/copy")
    public String copyCoshhAssessment(@RequestParam UUID id, Model model) {
        var coshhAssessment = assessmentService.findOneCoshhAssessment(id);
        coshhAssessment.setAssessmentId(null);
        model.addAttribute(COSHH_ASSESSMENT, coshhAssessment);

        setCoshhAssessmentFields(model);

        model.addAttribute(PAGE_TITLE, "Copy: " + coshhAssessment.getAssessmentName());

        return "assessments/copyCoshhAssessment";

    }

    @GetMapping("/assessment/coshh/delete")
    public String deleteCoshhAssessment(@RequestParam UUID id, Model model) {

        var coshhAssessment = assessmentService.findOneCoshhAssessment(id);

        model.addAttribute(COSHH_ASSESSMENT, coshhAssessment);
        model.addAttribute(PAGE_TITLE, "Delete: " + coshhAssessment.getAssessmentName());
        return "assessments/deleteCoshhAssessment";
    }

    @GetMapping("/assessment/risk")
    public String riskAssessment(@RequestParam UUID id, Model model) {
        LabTrackUser user = userService.getCurrentUser();
        var riskAssessment = assessmentService.findOneRiskAssessment(id);
        model.addAttribute(RISK_ASSESSMENT, riskAssessment);
        model.addAttribute("canUserApprove", LabTrackUtilities.canUserApprove(user));
        model.addAttribute("isUserOwner", riskAssessment.getAuthor() == user);
        model.addAttribute("approved", riskAssessment.getStatus() != AssessmentState.PENDING);

        model.addAttribute(PAGE_TITLE,riskAssessment.getAssessmentName());
        return "assessments/riskAssessment";
    }

    @GetMapping("/assessment/risk/copy")
    public String copyRiskAssessment(@RequestParam UUID id, Model model) {
        var riskAssessment = assessmentService.findOneRiskAssessment(id);
        riskAssessment.setAssessmentId(null);
        model.addAttribute(RISK_ASSESSMENT, riskAssessment);

        setRiskAssessmentFields(model);

        model.addAttribute(PAGE_TITLE, "Copy: " + riskAssessment.getAssessmentName());

        return "assessments/copyRiskAssessment";

    }

    private void setRiskAssessmentFields(Model model) {
        model.addAttribute("chemicalHazardCards", cardService.findAllChemicalHazardCards());
        model.addAttribute("biologicalHazardCards", cardService.findAllBiologicalHazardCards());
        model.addAttribute("physicalHazardCards", cardService.findAllPhysicalHazardCards());
        model.addAttribute("frequencies", FrequencyOfTask.values());
        model.addAttribute("severities", Severity.values());
        model.addAttribute("likelihoods", Likelihood.values());
    }

    @GetMapping("/assessment/risk/delete")
    public String deleteRiskAssessment(@RequestParam UUID id, Model model) {

        var riskAssessment = assessmentService.findOneRiskAssessment(id);

        model.addAttribute(RISK_ASSESSMENT, riskAssessment);
        model.addAttribute(PAGE_TITLE, "Delete: " + riskAssessment.getAssessmentName());
        return "assessments/deleteRiskAssessment";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute(PAGE_TITLE,"Error");
        return "error";
    }
}
