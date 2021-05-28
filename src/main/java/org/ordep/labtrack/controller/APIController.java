package org.ordep.labtrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.exception.UserException;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.ChemicalHazardCard;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.RiskAssessment;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        String password = data.getFirst("password");

        if (username == null || !username.matches(EMAIL_REGEX)) {
            throw new UserException("Email does not meet requirements for user: " + username);
        }

        if (password == null ||  !password.matches(PASSWORD_REGEX)) {
            throw new UserException("Password does not meet requirements for user:" + username);
        }

        String hashedPassword = passwordEncoder.encode(data.getFirst("password"));

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
    public String submitRiskAssessment(@ModelAttribute RiskAssessment riskAssessment, Model model) {
        log.info("New Risk Assessment: {}", riskAssessment);
        riskAssessment.setAssessmentId(UUID.randomUUID());
        assessmentService.newRiskAssessment(riskAssessment);
        return "/home";
    }

    @PostMapping("/api/chemical/new")
    public void newCard(@ModelAttribute ChemicalHazardCard chemicalHazardCard, Model model, HttpServletResponse httpServletResponse) {
        log.info("New Chemical Hazard Card: {}", chemicalHazardCard);
        model.addAttribute(chemicalHazardCard);
        cardService.newChemicalHazardCard(chemicalHazardCard);

        httpServletResponse.setHeader("Location", "/home");
        httpServletResponse.setStatus(302);

    }
}
