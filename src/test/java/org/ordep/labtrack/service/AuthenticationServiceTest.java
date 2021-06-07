package org.ordep.labtrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ordep.labtrack.data.AuthenticationRepository;
import org.ordep.labtrack.exception.UserException;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationRepository authenticationRepository;
    private AuthenticationService authenticationService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private AuthenticationEntity authenticationEntity;
    private LabTrackUser user;

    @BeforeEach
    void setUp() throws IOException {
        authenticationRepository = Mockito.mock(AuthenticationRepository.class);
        authenticationService = new AuthenticationService(authenticationRepository);
        objectMapper.registerModule(new JavaTimeModule());
        authenticationEntity = objectMapper.readValue(new ClassPathResource("/json/AuthenticationEntity.json").getInputStream(), AuthenticationEntity.class);
        user = objectMapper.readValue(new ClassPathResource("/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);

    }

    @Test
    void loadUserByUsername() {
        Mockito.when(authenticationRepository.getAuthenticationEntityByUsername(anyString())).thenReturn(authenticationEntity);

        assertEquals(authenticationEntity, authenticationService.loadUserByUsername("user"));
    }

    @Test
    void registerUser() {
        authenticationService.saveAuthenticationEntity(authenticationEntity);
        verify(authenticationRepository, times(1)).save(any());
    }

    @Test
    void getAuthenticationEntity() {
        Mockito.when(authenticationRepository.getAuthenticationEntityByUsername(anyString())).thenReturn(authenticationEntity);

        assertEquals(authenticationEntity, authenticationService.getAuthenticationEntity("user"));
    }

    @Test
    void canUserApprove_true() {

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertTrue(authenticationService.canUserApprove(user));
    }

    @Test
    void canUserApprove_false() {

        authenticationEntity.setRoles(Collections.singletonList(Role.USER));

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertFalse(authenticationService.canUserApprove(user));
    }

    @Test
    void getUserRoles() {
        List<Role> roles = Arrays.asList(Role.ADMIN, Role.USER);

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertEquals(roles, authenticationService.getUserRoles(user));
    }

    @Test
    void getUserRoles_noRoles() {
        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertEquals(Collections.emptyList(), authenticationService.getUserRoles(user));
    }

    @Test
    void getHighestRole_admin() {

        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertEquals(Role.ADMIN, authenticationService.getHighestRole(user));
    }

    @Test
    void getHighestRole_lecturer() {

        authenticationEntity.setRoles(Collections.singletonList(Role.LECTURER));
        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertEquals(Role.LECTURER, authenticationService.getHighestRole(user));
    }

    @Test
    void getHighestRole_student() {

        authenticationEntity.setRoles(Collections.singletonList(Role.STUDENT));
        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertEquals(Role.STUDENT, authenticationService.getHighestRole(user));
    }

    @Test
    void getHighestRole_user() {

        authenticationEntity.setRoles(Collections.singletonList(Role.USER));
        Mockito.when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertEquals(Role.USER, authenticationService.getHighestRole(user));
    }

    @Test
    void findUserById() {
        when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.of(authenticationEntity));

        assertEquals(authenticationEntity, authenticationService.findUserById(UUID.randomUUID()));
    }

    @Test
    void findUserById_EmptyOptional() {
        when(authenticationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID uuid = UUID.randomUUID();

        assertThrows(UserException.class,() -> authenticationService.findUserById(uuid));
    }
}