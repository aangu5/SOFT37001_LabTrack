package org.ordep.labtrack.data;

import org.ordep.labtrack.model.ChemicalHazardCard;
import org.ordep.labtrack.model.LabTrackUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ChemicalHazardCardRepository extends PagingAndSortingRepository<ChemicalHazardCard, UUID> {
    List<ChemicalHazardCard> findAll();
    List<ChemicalHazardCard> findAllByOrderByDateCreatedDesc();

    List<ChemicalHazardCard> findChemicalHazardCardsByAuthor(LabTrackUser user, Pageable pageable);
    List<ChemicalHazardCard> findByCardNameContainsIgnoreCase(String cardName);
    List<ChemicalHazardCard> findByAuthor_DisplayNameContainsIgnoreCase(String cardName);
}
