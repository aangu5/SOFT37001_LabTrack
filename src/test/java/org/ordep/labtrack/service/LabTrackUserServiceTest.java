package org.ordep.labtrack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ordep.labtrack.data.UserRepository;
import org.ordep.labtrack.exception.UserNotFoundException;
import org.ordep.labtrack.model.LabTrackUser;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LabTrackUserServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void findUser() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);
        LabTrackUser testLabTrackUser = new LabTrackUser();
        testLabTrackUser.setUserId(UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497"));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(testLabTrackUser));

        assertEquals(testLabTrackUser,userService.findUser(UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497")));

    }

    @Test
    void findUser_throwsUserNotFoundException() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UUID uuid = UUID.fromString("24644ebd-cd65-44b1-baa8-435c5d03d497");

        assertThrows(UserNotFoundException.class, () -> userService.findUser(uuid));

    }

    @Test
    void updateUser() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);
        userService.updateUser(new LabTrackUser());
        verify(userRepository, times(1)).save(any());
    }
}