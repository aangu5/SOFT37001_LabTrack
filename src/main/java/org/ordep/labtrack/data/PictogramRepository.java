package org.ordep.labtrack.data;

import org.ordep.labtrack.model.Pictogram;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface PictogramRepository extends PagingAndSortingRepository<Pictogram, UUID> {
    List<Pictogram> findByPictogramIdIn(List<UUID> uuids);
}
