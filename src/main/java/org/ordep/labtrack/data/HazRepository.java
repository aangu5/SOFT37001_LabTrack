package org.ordep.labtrack.data;

import org.ordep.labtrack.model.Haz;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface HazRepository extends PagingAndSortingRepository<Haz, UUID> {
    List<Haz> findAllByDataIdIn(List<UUID>ids);
}
