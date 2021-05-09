package org.ordep.labtrack.data;

import org.ordep.labtrack.model.Man;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ManRepository extends PagingAndSortingRepository<Man, UUID> {
    List<Man> findAllByDataIdIn(List<UUID> ids);
    Man findByDataId(UUID uuid);
}
