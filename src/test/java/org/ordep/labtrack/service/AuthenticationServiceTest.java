package org.ordep.labtrack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ordep.labtrack.data.AuthenticationRepository;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        authenticationService.registerUser(new AuthenticationEntity());
        verify(authenticationRepository, times(1)).save(any());
    }
}