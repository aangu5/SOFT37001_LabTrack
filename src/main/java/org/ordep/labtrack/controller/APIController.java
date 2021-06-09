package org.ordep.labtrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.configuration.LabTrackUtilities;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.ordep.labtrack.configuration.Constants.HOME_URL;
import static org.ordep.labtrack.configuration.Constants.REDIRECT_LOCATION;
import static org.ordep.labtrack.configuration.Constants.EMAIL_REGEX;
import static org.ordep.labtrack.configuration.Constants.PASSWORD_REGEX;

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

        authenticationService.saveAuthenticationEntity(authenticationEntity);

        var labTrackUser = new LabTrackUser();
        labTrackUser.setUserId(userID);
        labTrackUser.setEmailAddress(username);
        labTrackUser.setDisplayName(data.getFirst("displayName"));
        labTrackUser.setLoggedIn(false);
        labTrackUser.setRoles(Collections.singletonList(Role.USER));

        userService.registerUser(labTrackUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody MultiValueMap<String, String> data) {
        LabTrackUser currentUser = userService.getCurrentUser();

        log.info("Changing user password for user: {}", currentUser.getUserId());

        String oldPassword = data.getFirst("old-password");
        String newPassword1 = data.getFirst("new-password-1");
        String newPassword2 = data.getFirst("new-password-2");

        if (oldPassword == null || newPassword1 == null) {
            throw new UserException("Null value for username or password for user: " + currentUser.getDisplayName());
        }

        if (!newPassword1.equals(newPassword2)) {
            throw new UserException("Passwords do not match for user: " + currentUser.getDisplayName());
        }

        if (!newPassword1.matches(PASSWORD_REGEX)) {
            throw new UserException("Password does not meet requirements for user:" + currentUser.getDisplayName());
        }

        var userDetails = authenticationService.getAuthenticationEntity(currentUser.getEmailAddress());

        if (!passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
            throw new UserException("Invalid current password for user: " + currentUser.getDisplayName());
        }

        String hashedPassword = passwordEncoder.encode(newPassword1);

        userDetails.setPassword(hashedPassword);

        authenticationService.saveAuthenticationEntity(userDetails);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/user/change-role")
    public void changeRole(@RequestParam UUID userId, @RequestParam Role role) {
        var currentUser = userService.getCurrentUser();
        var user = userService.findUser(userId);
        var auth = authenticationService.findUserById(userId);

        if (user == null || auth == null) {
            throw new UserException("User not found");
        }

        if (LabTrackUtilities.isUserSenior(user.getRoles().get(0), currentUser.getRoles().get(0))) {
            throw new UserException("User does not have the right permission to move user: " + user);
        }

        var approvalLevel = LabTrackUtilities.getJuniorRoles(currentUser.getRoles().get(0));

        if (!approvalLevel.contains(role)) {
            throw new UserException("User does not have the right permission for role: " + role.getDisplayName());
        }

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);

        userService.updateUser(user);

        auth.setRoles(roles);

        authenticationService.saveAuthenticationEntity(auth);
    }

    @PostMapping("/api/user/change-name")
    public void changeDisplayName(@RequestParam String displayName) {
        var currentUser = userService.getCurrentUser();

        currentUser.setDisplayName(displayName);

        userService.updateUser(currentUser);
    }

    @GetMapping("/api/risks")
    public List<RiskAssessment> getRisks() {
        return assessmentService.getAllRiskAssessments();
    }

    @PostMapping("/api/assessment/risk/new")
    public void submitRiskAssessment(@ModelAttribute RiskAssessment riskAssessment, HttpServletResponse httpServletResponse) {
        log.info("New Risk Assessment: {}", riskAssessment);

        assessmentService.newRiskAssessment(riskAssessment);

        httpServletResponse.setHeader(REDIRECT_LOCATION, HOME_URL);
        httpServletResponse.setStatus(302);
    }

    @PostMapping("/api/assessment/risk/approve")
    public void approveRiskAssessment(@RequestParam UUID assessmentId, HttpServletResponse httpServletResponse) {

        var assessment = assessmentService.findOneRiskAssessment(assessmentId);
        var user = userService.getCurrentUser();

        log.info("Risk Assessment approval: {} by {}", assessment, user);

        if (LabTrackUtilities.canUserApprove(user)) {
            assessment.setApproved(true);
            assessment.setDateApproved(LocalDateTime.now());
            assessment.setApprover(user);

            assessmentService.updateRiskAssessment(assessment);
        }
    }

    @DeleteMapping("/api/assessment/risk")
    public void deleteRiskAssessment(@RequestParam UUID assessmentId) {
        var assessment = assessmentService.findOneRiskAssessment(assessmentId);
        var user = userService.getCurrentUser();

        if (assessment.getAuthor().equals(user)) {
            log.info("Risk Assessment to be deleted: {} by: {}", assessment, user);
            assessmentService.deleteRiskAssessment(assessment);
        }

    }

    @PostMapping("/api/assessment/coshh/new")
    public void submitCoshhAssessment(@ModelAttribute CoshhAssessment coshhAssessment, HttpServletResponse httpServletResponse) {
        log.info("New Coshh Assessment: {}", coshhAssessment);

        assessmentService.newCoshhAssessment(coshhAssessment);

        httpServletResponse.setHeader(REDIRECT_LOCATION, HOME_URL);
        httpServletResponse.setStatus(302);
    }

    @PostMapping("/api/assessment/coshh/approve")
    public void approveCoshhAssessment(@RequestParam UUID assessmentId, HttpServletResponse httpServletResponse) {

        var assessment = assessmentService.findOneCoshhAssessment(assessmentId);
        var user = userService.getCurrentUser();

        log.info("Coshh Assessment approval: {} by {}", assessment, user);

        if (LabTrackUtilities.canUserApprove(user)) {
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

    @DeleteMapping("/api/assessment/coshh")
    public void deleteCoshhAssessment(@RequestParam UUID assessmentId) {
        var assessment = assessmentService.findOneCoshhAssessment(assessmentId);
        var user = userService.getCurrentUser();

        if (assessment.getAuthor() == user) {
            log.info("Risk Assessment to be deleted: {} by: {}", assessment, user);
            assessmentService.deleteCoshhAssessment(assessment);
        }

    }

    @PostMapping("/api/card/chemical/new")
    public void newChemicalHazardCard(@ModelAttribute ChemicalHazardCard chemicalHazardCard, Model model, HttpServletResponse httpServletResponse) {
        log.info("New Chemical Hazard Card: {}", chemicalHazardCard);
        model.addAttribute(chemicalHazardCard);
        cardService.newChemicalHazardCard(chemicalHazardCard);

        httpServletResponse.setHeader(REDIRECT_LOCATION, HOME_URL);
        httpServletResponse.setStatus(302);

    }

    @PostMapping("/api/card/biological/new")
    public void newBiologicalHazardCard(@ModelAttribute BiologicalHazardCard biologicalHazardCard, Model model, HttpServletResponse httpServletResponse) {
        log.info("New Biological Hazard Card: {}", biologicalHazardCard);
        model.addAttribute(biologicalHazardCard);
        cardService.newBiologicalHazardCard(biologicalHazardCard);

        httpServletResponse.setHeader(REDIRECT_LOCATION, HOME_URL);
        httpServletResponse.setStatus(302);

    }

    @PostMapping("/api/card/physical/new")
    public void newPhysicalHazardCard(@ModelAttribute PhysicalHazardCard physicalHazardCard, Model model, HttpServletResponse httpServletResponse) {
        log.info("New Physical Hazard Card: {}", physicalHazardCard);
        model.addAttribute(physicalHazardCard);
        cardService.newPhysicalHazardCard(physicalHazardCard);

        httpServletResponse.setHeader(REDIRECT_LOCATION, HOME_URL);
        httpServletResponse.setStatus(302);

    }
}
