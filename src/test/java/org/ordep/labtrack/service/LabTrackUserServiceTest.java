package org.ordep.labtrack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ordep.labtrack.data.UserRepository;
import org.ordep.labtrack.exception.UserException;
import org.ordep.labtrack.model.LabTrackUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LabTrackUserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void findUser() throws Exception {
        LabTrackUser testLabTrackUser = new LabTrackUser();
        testLabTrackUser.setUserId(UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497"));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(testLabTrackUser));

        assertEquals(testLabTrackUser,userService.findUser(UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497")));

    }

    @Test
    void findUser_throwsUserNotFoundException() throws Exception {

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID uuid = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");

        assertThrows(UserException.class, () -> userService.findUser(uuid));

    }

    @Test
    void updateUser() {
        userService.updateUser(new LabTrackUser());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUser() {
        userService.registerUser(new LabTrackUser());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getCurrentUser_ValidUser() {
        UUID expectedUserID = UUID.randomUUID();

        LabTrackUser user = new LabTrackUser();
        user.setDisplayName("Steve");
        user.setUserId(expectedUserID);

        when(userRepository.findByEmailAddress(anyString())).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("Steve");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        UUID userId = userService.getCurrentUser();

        assertEquals(expectedUserID, userId);

        verify(userRepository, times(1)).findByEmailAddress("Steve");
    }

    @Test
    void getCurrentUser_UserNull() {
        UUID expectedUserID = UUID.randomUUID();

        LabTrackUser user = new LabTrackUser();
        user.setDisplayName("Steve");
        user.setUserId(expectedUserID);

        when(userRepository.findByEmailAddress(anyString())).thenReturn(null);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("Steve");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertThrows(UserException.class, () -> userService.getCurrentUser());

        verify(userRepository, times(1)).findByEmailAddress("Steve");
    }
}