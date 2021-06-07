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

    public boolean canUserApprove(LabTrackUser user) {
        List<Role> roles = getUserRoles(user);

        return roles.contains(Role.ADMIN) || roles.contains(Role.LECTURER);
    }

    public List<Role> getUserRoles(LabTrackUser user) {
        Optional<AuthenticationEntity> optional = authenticationRepository.findById(user.getUserId());

        if (optional.isEmpty()) {
            return new ArrayList<>();
        }

        AuthenticationEntity entity = optional.get();
        return entity.getRoles();
    }

    public Role getHighestRole(LabTrackUser user){
        List<Role> roles = getUserRoles(user);
        if (roles.contains(Role.ADMIN)) {
            return Role.ADMIN;
        }
        else if (roles.contains(Role.LECTURER)){
            return Role.LECTURER;
        }
        else if (roles.contains(Role.STUDENT)){
            return Role.STUDENT;
        }
        else {
            return Role.USER;
        }
    }
}
