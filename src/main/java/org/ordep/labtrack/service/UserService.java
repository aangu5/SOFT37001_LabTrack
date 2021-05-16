package org.ordep.labtrack.service;

import org.ordep.labtrack.data.UserRepository;
import org.ordep.labtrack.exception.UserNotFoundException;
import org.ordep.labtrack.model.LabTrackUser;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LabTrackUser findUser(UUID userID) {
        Optional<LabTrackUser> optional = userRepository.findById(userID);

        if (optional.isEmpty()) {
            throw new UserNotFoundException(userID);
        }

        return optional.get();
    }

    public void updateUser(LabTrackUser updatedLabTrackUser){
        userRepository.save(updatedLabTrackUser);
    }

    public void registerUser(LabTrackUser labTrackUser) {
        userRepository.save(labTrackUser);
    }
}
