package org.ordep.labtrack.service;

import org.ordep.labtrack.data.AuthenticationRepository;
import org.ordep.labtrack.model.AuthenticationEntity;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public boolean canUserApprove(LabTrackUser user) {
        Optional<AuthenticationEntity> optional = authenticationRepository.findById(user.getUserId());

        if (optional.isEmpty()) {
            return false;
        }

        AuthenticationEntity entity = optional.get();
        List<Role> roles = entity.getRoles();

        return roles.contains(Role.ADMIN) || roles.contains(Role.LECTURER);
    }
}
