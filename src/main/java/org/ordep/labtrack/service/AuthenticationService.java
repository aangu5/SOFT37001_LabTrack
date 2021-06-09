package org.ordep.labtrack.service;

import org.ordep.labtrack.data.AuthenticationRepository;
import org.ordep.labtrack.exception.UserException;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService implements UserDetailsService {

    private final AuthenticationRepository authenticationRepository;

    public AuthenticationService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationRepository.getAuthenticationEntityByUsername(username);
    }

    public AuthenticationEntity findUserById(UUID userId) {
        var auth = authenticationRepository.findById(userId);

        if (auth.isPresent()) {
            return auth.get();
        }
        throw new UserException("User not found for ID: " + userId);
    }

    public AuthenticationEntity getAuthenticationEntity(String username) {
        return authenticationRepository.getAuthenticationEntityByUsername(username);
    }

    public void saveAuthenticationEntity(AuthenticationEntity authenticationEntity) {
        authenticationRepository.save(authenticationEntity);
    }
}
