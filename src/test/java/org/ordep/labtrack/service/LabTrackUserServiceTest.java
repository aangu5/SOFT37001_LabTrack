package org.ordep.labtrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.UserRepository;
import org.ordep.labtrack.exception.UserException;
import org.ordep.labtrack.model.LabTrackUser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LabTrackUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private LabTrackUser user;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());

        user = objectMapper.readValue(new ClassPathResource(
                "/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);
    }

    @Test
    void findUser() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        assertEquals(user, userService.findUser(UUID.randomUUID()));

    }

    @Test
    void findUser_throwsUserNotFoundException() {

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        UUID uuid = UUID.randomUUID();

        assertThrows(UserException.class, () -> userService.findUser(uuid));

    }

    @Test
    void updateUser() {
        userService.updateUser(user);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUser() {
        userService.registerUser(user);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getCurrentUser_ValidUser() {

        when(userRepository.findByEmailAddress(anyString())).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("Steve");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        LabTrackUser currentUser = userService.getCurrentUser();

        assertEquals(user, currentUser);

        verify(userRepository, times(1)).findByEmailAddress("Steve");
    }

    @Test
    void getCurrentUser_UserNull() {

        when(userRepository.findByEmailAddress(anyString())).thenReturn(null);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("Steve");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertThrows(UserException.class, () -> userService.getCurrentUser());

        verify(userRepository, times(1)).findByEmailAddress("Steve");
    }

    @Test
    void getAllUsers() {

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        assertEquals(Collections.singletonList(user),userService.getAllUsers());

    }
}