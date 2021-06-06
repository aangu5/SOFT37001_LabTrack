package org.ordep.labtrack.data;

import org.ordep.labtrack.model.AuthenticationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AuthenticationRepository extends CrudRepository<AuthenticationEntity, UUID> {
    AuthenticationEntity getAuthenticationEntityByUsername(String username);
    List<AuthenticationEntity> findAll();
}
