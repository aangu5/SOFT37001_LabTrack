package org.ordep.labtrack.data;

import org.ordep.labtrack.model.HazardStatement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface HazardStatementRepository extends CrudRepository<HazardStatement, UUID> {
    List<HazardStatement> findByStatementIdIn(List<UUID> uuids);
}
