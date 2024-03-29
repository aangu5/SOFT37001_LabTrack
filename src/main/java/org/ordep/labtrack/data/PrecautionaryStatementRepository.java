package org.ordep.labtrack.data;

import org.ordep.labtrack.model.PrecautionaryStatement;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface PrecautionaryStatementRepository extends PagingAndSortingRepository<PrecautionaryStatement, UUID> {
    List<PrecautionaryStatement> findByStatementIdIn(List<UUID> uuids);
}

