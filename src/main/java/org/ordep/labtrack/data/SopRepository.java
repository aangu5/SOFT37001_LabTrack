package org.ordep.labtrack.data;

import org.ordep.labtrack.model.Sop;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface SopRepository extends PagingAndSortingRepository<Sop, UUID> {
    List<Sop> findAllByDataIdIn(List<UUID> ids);
}
