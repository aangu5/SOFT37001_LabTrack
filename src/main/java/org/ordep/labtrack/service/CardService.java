package org.ordep.labtrack.service;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.data.*;
import org.ordep.labtrack.exception.CardNotFoundException;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.ordep.labtrack.configuration.Constants.PAGE_COUNT;

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

    // Chemical Hazard Cards

    public ChemicalHazardCard newChemicalHazardCard(ChemicalHazardCard chemicalHazardCard) {

        LabTrackUser user = userService.getCurrentUser();

        chemicalHazardCard.setCardId(UUID.randomUUID());
        chemicalHazardCard.setAuthor(user);
        chemicalHazardCard.setDateCreated(LocalDateTime.now());
        chemicalHazardCard.setCardType(CardType.CHEMICAL);

        chemicalHazardCardRepository.save(chemicalHazardCard);
        log.info("Chemical Hazard Card created: {}", chemicalHazardCard);
        return chemicalHazardCard;
    }

    public List<ChemicalHazardCard> findAllChemicalHazardCards() {
        return chemicalHazardCardRepository.findAll();
    }

    public List<ChemicalHazardCard> findAllChemicalHazardCardsForUser(UUID userID, int page) {
        LabTrackUser user = userService.findUser(userID);
        Pageable pageable = PageRequest.of(page, PAGE_COUNT);
        return chemicalHazardCardRepository.findChemicalHazardCardsByAuthor(user, pageable);
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
        physicalHazardCard.setAuthor(user);
        physicalHazardCard.setDateCreated(LocalDateTime.now());
        physicalHazardCard.setCardType(CardType.PHYSICAL);

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
        biologicalHazardCard.setAuthor(user);
        biologicalHazardCard.setDateCreated(LocalDateTime.now());
        biologicalHazardCard.setCardType(CardType.BIOLOGICAL);

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

        output.addAll(chemicalHazardCardRepository.findAllByOrderByDateCreatedDesc());
        output.addAll(physicalHazardCardRepository.findAllByOrderByDateCreatedDesc());
        output.addAll(biologicalHazardCardRepository.findAllByOrderByDateCreatedDesc());

        output.sort(Comparator.comparing(Card::getDateCreated).reversed());
        return output;
    }

    public List<Card> searchCardName(String name) {
        List<Card> output = new ArrayList<>();

        output.addAll(chemicalHazardCardRepository.findByCardNameContainsIgnoreCase(name));
        output.addAll(biologicalHazardCardRepository.findByCardNameContainsIgnoreCase(name));
        output.addAll(physicalHazardCardRepository.findByCardNameContainsIgnoreCase(name));

        output.sort(Comparator.comparing(Card::getDateCreated).reversed());
        return output;
    }

    public List<Card> searchCardAuthor(String author) {
        List<Card> output = new ArrayList<>();

        output.addAll(chemicalHazardCardRepository.findByAuthor_DisplayNameContainsIgnoreCase(author));
        output.addAll(biologicalHazardCardRepository.findByAuthor_DisplayNameContainsIgnoreCase(author));
        output.addAll(physicalHazardCardRepository.findByAuthor_DisplayNameContainsIgnoreCase(author));

        output.sort(Comparator.comparing(Card::getDateCreated).reversed());
        return output;
    }

}
