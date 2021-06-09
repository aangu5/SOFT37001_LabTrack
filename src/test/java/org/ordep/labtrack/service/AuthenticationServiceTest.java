package org.ordep.labtrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.AuthenticationRepository;
import org.ordep.labtrack.exception.UserException;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationRepository authenticationRepository;
    @InjectMocks
    private AuthenticationService authenticationService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private AuthenticationEntity authenticationEntity;
    private LabTrackUser user;

    @BeforeEach
    void setUp() throws IOException {
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
        verify(authenticationRepository, times(1)).save(any(AuthenticationEntity.class));
    }

    @Test
    void getAuthenticationEntity() {
        Mockito.when(authenticationRepository.getAuthenticationEntityByUsername(anyString())).thenReturn(authenticationEntity);

        assertEquals(authenticationEntity, authenticationService.getAuthenticationEntity("user"));
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