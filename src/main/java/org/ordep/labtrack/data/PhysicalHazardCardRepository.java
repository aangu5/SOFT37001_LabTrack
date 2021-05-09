package org.ordep.labtrack.data;

import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.PhysicalHazardCard;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface PhysicalHazardCardRepository extends PagingAndSortingRepository<PhysicalHazardCard, UUID> {
    List<PhysicalHazardCard> findAll();

    List<PhysicalHazardCard> findPhysicalHazardCardsByAuthor(LabTrackUser user);
}
