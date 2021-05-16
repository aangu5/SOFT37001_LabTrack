package org.ordep.labtrack.service;

import org.ordep.labtrack.data.AuthenticationRepository;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public void registerUser(AuthenticationEntity authenticationEntity) {
        authenticationRepository.save(authenticationEntity);
    }
}
