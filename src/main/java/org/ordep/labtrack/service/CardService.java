package org.ordep.labtrack.service;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.data.*;
import org.ordep.labtrack.exception.CardNotFoundException;
import org.ordep.labtrack.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CardService {

    @Autowired
    private ChemicalHazardCardRepository chemicalHazardCardRepository;
    @Autowired
    private StatementService statementService;
    @Autowired
    private UserService userService;
    @Autowired
    private PhysicalHazardCardRepository physicalHazardCardRepository;
    @Autowired
    private BiologicalHazardCardRepository biologicalHazardCardRepository;
    @Autowired
    private SymRepository symRepository;
    @Autowired
    private HazRepository hazRepository;
    @Autowired
    private ManRepository manRepository;
    @Autowired
    private SopRepository sopRepository;

    // Chemical Hazard Cards

    public ChemicalHazardCard newChemicalHazardCard(ChemicalHazardCard chemicalHazardCard) {

        LabTrackUser user = userService.getCurrentUser();

        chemicalHazardCard.setCardId(UUID.randomUUID());
        chemicalHazardCard.setAuthor(user);
        chemicalHazardCard.setStatus(true);
        chemicalHazardCard.setDateCreated(LocalDateTime.now());

        chemicalHazardCardRepository.save(chemicalHazardCard);
        log.info("Chemical Hazard Card created: {}", chemicalHazardCard);
        return chemicalHazardCard;
    }

    public List<ChemicalHazardCard> findAllChemicalHazardCards() {
        return chemicalHazardCardRepository.findAll();
    }

    public List<ChemicalHazardCard> findAllChemicalHazardCardsForUser(UUID userID) {
        LabTrackUser user = userService.findUser(userID);
        return chemicalHazardCardRepository.findChemicalHazardCardsByAuthor(user);
    }

    public ChemicalHazardCard findOneChemicalHazardCard(UUID cardId) {
        Optional<ChemicalHazardCard> optional = chemicalHazardCardRepository.findById(cardId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CardNotFoundException(cardId);
    }

    // Physical Hazard Cards

    public PhysicalHazardCard newPhysicalHazardCard(PhysicalHazardCard physicalHazardCard) {

        LabTrackUser user = userService.getCurrentUser();

        physicalHazardCard.setCardId(UUID.randomUUID());
        physicalHazardCard.setStatus(true);
        physicalHazardCard.setAuthor(user);
        physicalHazardCard.setDateCreated(LocalDateTime.now());

        physicalHazardCardRepository.save(physicalHazardCard);
        log.info("Physical Hazard Card created: {}", physicalHazardCard);
        return physicalHazardCard;
    }

    public List<PhysicalHazardCard> findAllPhysicalHazardCards() {
        return physicalHazardCardRepository.findAll();
    }

    public List<PhysicalHazardCard> findAllPhysicalHazardCardsForUser(UUID userID) {
        LabTrackUser user = userService.findUser(userID);
        return physicalHazardCardRepository.findPhysicalHazardCardsByAuthor(user);
    }

    public PhysicalHazardCard findOnePhysicalHazardCard(UUID cardId) {
        Optional<PhysicalHazardCard> optional = physicalHazardCardRepository.findById(cardId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CardNotFoundException(cardId);
    }

    // Biology Hazard Cards

    public BiologicalHazardCard newBiologicalHazardCard(BiologicalHazardCard biologicalHazardCard) {

        LabTrackUser user = userService.getCurrentUser();

        biologicalHazardCard.setCardId(UUID.randomUUID());
        biologicalHazardCard.setStatus(true);
        biologicalHazardCard.setAuthor(user);
        biologicalHazardCard.setDateCreated(LocalDateTime.now());

        biologicalHazardCardRepository.save(biologicalHazardCard);
        log.info("Biological Hazard Card created: {}", biologicalHazardCard);
        return biologicalHazardCard;
    }

    public List<BiologicalHazardCard> findAllBiologicalHazardCards() {
        return biologicalHazardCardRepository.findAll();
    }

    public List<BiologicalHazardCard> findAllBiologicalHazardCardsForUser(UUID userID) {
        LabTrackUser user = userService.findUser(userID);
        return biologicalHazardCardRepository.findBiologicalHazardCardsByAuthor(user);
    }

    public BiologicalHazardCard findOneBiologicalHazardCard(UUID cardId) {
        Optional<BiologicalHazardCard> optional = biologicalHazardCardRepository.findById(cardId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new CardNotFoundException(cardId);
    }

    // Other Methods
    public List<Card> getAllCards() {
        List<Card> output = new ArrayList<>();

        output.addAll(chemicalHazardCardRepository.findAll());
        output.addAll(physicalHazardCardRepository.findAll());
        output.addAll(biologicalHazardCardRepository.findAll());

        return output;
    }

}
