package org.ordep.labtrack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ordep.labtrack.data.AuthenticationRepository;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {

    private AuthenticationRepository authenticationRepository;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationRepository = Mockito.mock(AuthenticationRepository.class);
        authenticationService = new AuthenticationService(authenticationRepository);

    }

    @Test
    void loadUserByUsername() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Collections.singletonList(Role.USER);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.getAuthenticationEntityByUsername(anyString())).thenReturn(auth);

        assertEquals(auth, authenticationService.loadUserByUsername("user"));
    }

    @Test
    void registerUser() {
        authenticationService.saveAuthenticationEntity(new AuthenticationEntity());
        verify(authenticationRepository, times(1)).save(any());
    }

    @Test
    void getAuthenticationEntity() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Collections.singletonList(Role.USER);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.getAuthenticationEntityByUsername(anyString())).thenReturn(auth);

        assertEquals(auth, authenticationService.getAuthenticationEntity("user"));
    }

    @Test
    void canUserApprove_true() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Collections.singletonList(Role.ADMIN);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(auth));

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertTrue(authenticationService.canUserApprove(user));
    }

    @Test
    void canUserApprove_false() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Collections.singletonList(Role.USER);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(auth));

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertFalse(authenticationService.canUserApprove(user));
    }

    @Test
    void getUserRoles() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Collections.singletonList(Role.USER);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(auth));

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertEquals(roles, authenticationService.getUserRoles(user));
    }

    @Test
    void getUserRoles_noRoles() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertEquals(Collections.emptyList(), authenticationService.getUserRoles(user));
    }

    @Test
    void getHighestRole_admin() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Arrays.asList(Role.USER, Role.ADMIN);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(auth));

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertEquals(Role.ADMIN, authenticationService.getHighestRole(user));
    }

    @Test
    void getHighestRole_lecturer() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Arrays.asList(Role.USER, Role.LECTURER);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(auth));

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertEquals(Role.LECTURER, authenticationService.getHighestRole(user));
    }

    @Test
    void getHighestRole_student() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Arrays.asList(Role.USER, Role.STUDENT);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(auth));

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertEquals(Role.STUDENT, authenticationService.getHighestRole(user));
    }

    @Test
    void getHighestRole_user() {
        UUID userId = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");
        List<Role> roles = Collections.singletonList(Role.USER);

        AuthenticationEntity auth = new AuthenticationEntity();
        auth.setUserId(userId);
        auth.setUsername("user");
        auth.setPassword("pass");
        auth.setActive(true);
        auth.setRoles(roles);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(auth));

        LabTrackUser user = new LabTrackUser();
        user.setUserId(userId);

        assertEquals(Role.USER, authenticationService.getHighestRole(user));
    }
}