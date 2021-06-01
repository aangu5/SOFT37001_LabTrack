package org.ordep.labtrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.exception.UserException;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.Role;
import org.ordep.labtrack.service.AssessmentService;
import org.ordep.labtrack.service.AuthenticationService;
import org.ordep.labtrack.service.CardService;
import org.ordep.labtrack.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.ordep.labtrack.configuration.Constants.HOME_URL;
import static org.ordep.labtrack.configuration.LabTrackUtilities.EMAIL_REGEX;
import static org.ordep.labtrack.configuration.LabTrackUtilities.PASSWORD_REGEX;

@Slf4j
@RestController
public class APIController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AssessmentService assessmentService;
    private final CardService cardService;

    public APIController(AuthenticationService authenticationService, UserService userService,
                         PasswordEncoder passwordEncoder, AssessmentService assessmentService, CardService cardService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.assessmentService = assessmentService;
        this.cardService = cardService;
    }

    @PostMapping(value = "/api/register", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public ResponseEntity<Void> createNewUser(@RequestBody MultiValueMap<String, String> data) {
        log.info("Creating new user!");
        var userID = UUID.randomUUID();

        String username = data.getFirst("username");
        String password1 = data.getFirst("password-1");
        String password2 = data.getFirst("password-2");

        if (username == null || password1 == null) {
            throw new UserException("Null value for username or password for user: " + username);
        }

        if (!password1.equals(password2)) {
            throw new UserException("Passwords do not match for user: " + username);
        }

        if (!username.matches(EMAIL_REGEX)) {
            throw new UserException("Email does not meet requirements for user: " + username);
        }

        if (!password1.matches(PASSWORD_REGEX)) {
            throw new UserException("Password does not meet requirements for user:" + username);
        }

        String hashedPassword = passwordEncoder.encode(password1);

        var authenticationEntity = new AuthenticationEntity();
        authenticationEntity.setUserId(userID);
        authenticationEntity.setUsername(username);
        authenticationEntity.setPassword(hashedPassword);
        authenticationEntity.setActive(true);
        authenticationEntity.setRoles(Collections.singletonList(Role.USER));

        authenticationService.registerUser(authenticationEntity);

        var labTrackUser = new LabTrackUser();
        labTrackUser.setUserId(userID);
        labTrackUser.setEmailAddress(username);
        labTrackUser.setDisplayName(data.getFirst("displayName"));
        labTrackUser.setLoggedIn(false);

        userService.registerUser(labTrackUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/risks")
    public List<RiskAssessment> getRisks() {
        return assessmentService.getAllRiskAssessments();
    }

    @PostMapping("/api/assessment/risk/new")
    public void submitRiskAssessment(@ModelAttribute RiskAssessment riskAssessment, HttpServletResponse httpServletResponse) {
        log.info("New Risk Assessment: {}", riskAssessment);

        assessmentService.newRiskAssessment(riskAssessment);

        httpServletResponse.setHeader("Location", HOME_URL);
        httpServletResponse.setStatus(200);
    }

    @PostMapping("/api/assessment/risk/approve")
    public void approveRiskAssessment(@RequestParam UUID assessmentId, HttpServletResponse httpServletResponse) {

        var assessment = assessmentService.findOneRiskAssessment(assessmentId);
        var user = userService.getCurrentUser();

        log.info("Risk Assessment approval: {} by {}", assessment, user);

        if (authenticationService.canUserApprove(user)) {
            assessment.setApproved(true);
            assessment.setDateApproved(LocalDateTime.now());
            assessment.setApprover(user);

            assessmentService.updateRiskAssessment(assessment);
        }
    }

    @PostMapping("/api/assessment/coshh/new")
    public void submitCoshhAssessment(@ModelAttribute CoshhAssessment coshhAssessment, HttpServletResponse httpServletResponse) {
        log.info("New Coshh Assessment: {}", coshhAssessment);

        assessmentService.newCoshhAssessment(coshhAssessment);

        httpServletResponse.setHeader("Location", HOME_URL);
        httpServletResponse.setStatus(302);
    }

    @PostMapping("/api/assessment/coshh/approve")
    public void approveCoshhAssessment(@RequestParam UUID assessmentId, HttpServletResponse httpServletResponse) {

        var assessment = assessmentService.findOneCoshhAssessment(assessmentId);
        var user = userService.getCurrentUser();

        log.info("Coshh Assessment approval: {} by {}", assessment, user);

        if (authenticationService.canUserApprove(user)) {
            assessment.setApproved(true);
            assessment.setDateApproved(LocalDateTime.now());
            assessment.setApprover(user);

            assessmentService.updateCoshhAssessment(assessment);
        }
    }

    @PostMapping("/api/assessment/coshh/sign")
    public void signCoshhAssessment(@RequestParam UUID assessmentId, HttpServletResponse httpServletResponse) {

        var assessment = assessmentService.findOneCoshhAssessment(assessmentId);
        var user = userService.getCurrentUser();

        if (assessmentService.canUserSignAssessment(assessment, user)) {

            log.info("Coshh Assessment signed: {} by {}", assessment, user);

            var users = assessment.getSignedUsers();
            users.add(user);
            assessment.setSignedUsers(users);

            assessmentService.updateCoshhAssessment(assessment);
        }
    }

    @PostMapping("/api/card/chemical/new")
    public void newChemicalHazardCard(@ModelAttribute ChemicalHazardCard chemicalHazardCard, Model model, HttpServletResponse httpServletResponse) {
        log.info("New Chemical Hazard Card: {}", chemicalHazardCard);
        model.addAttribute(chemicalHazardCard);
        cardService.newChemicalHazardCard(chemicalHazardCard);

        httpServletResponse.setHeader("Location", HOME_URL);
        httpServletResponse.setStatus(302);

    }

    @PostMapping("/api/card/biological/new")
    public void newBiologicalHazardCard(@ModelAttribute BiologicalHazardCard biologicalHazardCard, Model model, HttpServletResponse httpServletResponse) {
        log.info("New Biological Hazard Card: {}", biologicalHazardCard);
        model.addAttribute(biologicalHazardCard);
        cardService.newBiologicalHazardCard(biologicalHazardCard);

        httpServletResponse.setHeader("Location", HOME_URL);
        httpServletResponse.setStatus(302);

    }

    @PostMapping("/api/card/physical/new")
    public void newPhysicalHazardCard(@ModelAttribute PhysicalHazardCard physicalHazardCard, Model model, HttpServletResponse httpServletResponse) {
        log.info("New Biological Hazard Card: {}", physicalHazardCard);
        model.addAttribute(physicalHazardCard);
        cardService.newPhysicalHazardCard(physicalHazardCard);

        httpServletResponse.setHeader("Location", HOME_URL);
        httpServletResponse.setStatus(302);

    }
}
