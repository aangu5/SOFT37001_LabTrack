package org.ordep.labtrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;
import org.ordep.labtrack.service.AuthenticationService;
import org.ordep.labtrack.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@RestController
public class APIController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public APIController(AuthenticationService authenticationService, UserService userService,
                         PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/api/register", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public ResponseEntity<Void> createNewUser(@RequestBody MultiValueMap<String, String> data) {
        log.info("Creating new user!");
        var userID = UUID.randomUUID();

        String hashedPassword = passwordEncoder.encode(data.getFirst("password"));

        var authenticationEntity = new AuthenticationEntity();
        authenticationEntity.setUserId(userID);
        authenticationEntity.setUsername(data.getFirst("username"));
        authenticationEntity.setPassword(hashedPassword);
        authenticationEntity.setActive(true);
        authenticationEntity.setRoles(Collections.singletonList(Role.USER));

        authenticationService.registerUser(authenticationEntity);

        var labTrackUser = new LabTrackUser();
        labTrackUser.setUserId(userID);
        labTrackUser.setEmailAddress(data.getFirst("username"));
        labTrackUser.setDisplayName(data.getFirst("displayName"));
        labTrackUser.setLoggedIn(false);

        userService.registerUser(labTrackUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
