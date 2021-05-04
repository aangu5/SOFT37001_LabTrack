package org.ordep.labtrack.data;

import org.ordep.labtrack.model.LabTrackUser;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<LabTrackUser, UUID> {
}
