package org.ordep.labtrack.data;

import org.ordep.labtrack.model.BiologicalHazardCard;
import org.ordep.labtrack.model.LabTrackUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface BiologicalHazardCardRepository extends CrudRepository<BiologicalHazardCard, UUID> {
    @Override
    List<BiologicalHazardCard> findAll();

    List<BiologicalHazardCard> findAllByOrderByDateCreatedDesc();

    List<BiologicalHazardCard> findBiologicalHazardCardsByAuthor(LabTrackUser user);

    List<BiologicalHazardCard> findByCardNameContainsIgnoreCase(String name);

    List<BiologicalHazardCard> findByAuthor_DisplayNameContainsIgnoreCase(String author);
}
