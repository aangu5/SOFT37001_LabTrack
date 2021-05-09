package org.ordep.labtrack.data;

import org.ordep.labtrack.model.ChemicalHazardCard;
import org.ordep.labtrack.model.LabTrackUser;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ChemicalHazardCardRepository extends PagingAndSortingRepository<ChemicalHazardCard, UUID> {
    List<ChemicalHazardCard> findAll();

    List<ChemicalHazardCard> findChemicalHazardCardsByAuthor(LabTrackUser user);
}
