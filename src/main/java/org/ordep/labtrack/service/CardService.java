package org.ordep.labtrack.service;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.data.*;
import org.ordep.labtrack.exception.CardNotFoundException;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.PictogramType;
import org.ordep.labtrack.model.enums.SignalWord;
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
    private PictogramRepository pictogramRepository;
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

    public ChemicalHazardCard newChemicalHazardCard(String cardName, UUID userId, String cas, List<PictogramType> pictograms,
                                                    List<UUID> hazardStatementIDs,
                                                    List<UUID> precautionaryStatementIDs, List<String> synonyms, SignalWord signalWord) {

        List<HazardStatement> hazardStatements = statementService.findHazardStatements(hazardStatementIDs);
        List<PrecautionaryStatement> precautionaryStatements =
                statementService.findPrecautionaryStatements(precautionaryStatementIDs);
        LabTrackUser user = userService.findUser(userId);

        var chemicalHazardCard = new ChemicalHazardCard();
        chemicalHazardCard.setCardId(UUID.randomUUID());
        chemicalHazardCard.setCardName(cardName);
        chemicalHazardCard.setCas(cas);
        chemicalHazardCard.setPictograms(pictograms);
        chemicalHazardCard.setHazardStatements(hazardStatements);
        chemicalHazardCard.setPrecautionaryStatements(precautionaryStatements);
        chemicalHazardCard.setAuthor(user);
        chemicalHazardCard.setStatus(true);
        chemicalHazardCard.setDateCreated(LocalDateTime.now());
        chemicalHazardCard.setSynonyms(synonyms);
        chemicalHazardCard.setSignalWord(signalWord);


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

    public PhysicalHazardCard newPhysicalHazardCard(String cardName, UUID userId, List<PictogramType> pictograms,
                                                    List<UUID> symIds,
                                                    List<UUID> hazIds, List<UUID> manIds, List<UUID> sopIds) {

        List<Sym> syms = symRepository.findByDataIdIn(symIds);
        List<Haz> hazs = hazRepository.findAllByDataIdIn(hazIds);
        List<Man> men = manRepository.findAllByDataIdIn(manIds);
        List<Sop> sops = sopRepository.findAllByDataIdIn(sopIds);

        LabTrackUser user = userService.findUser(userId);

        var physicalHazardCard = new PhysicalHazardCard();
        physicalHazardCard.setCardId(UUID.randomUUID());
        physicalHazardCard.setCardName(cardName);
        physicalHazardCard.setPictograms(pictograms);
        physicalHazardCard.setStatus(true);
        physicalHazardCard.setAuthor(user);
        physicalHazardCard.setSyms(syms);
        physicalHazardCard.setHazs(hazs);
        physicalHazardCard.setMen(men);
        physicalHazardCard.setSops(sops);
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

    public BiologicalHazardCard newBiologicalHazardCard(String cardName, UUID userId, List<PictogramType> pictograms, UUID symId,
                                                        UUID manId, List<UUID> sopIds, String cat, String dose, String period,
                                                        String state) {

        var sym = symRepository.findByDataId(symId);
        var man = manRepository.findByDataId(manId);
        List<Sop> sops = sopRepository.findAllByDataIdIn(sopIds);

        LabTrackUser user = userService.findUser(userId);

        var biologicalHazardCard = new BiologicalHazardCard();
        biologicalHazardCard.setCardId(UUID.randomUUID());
        biologicalHazardCard.setCardName(cardName);
        biologicalHazardCard.setPictograms(pictograms);
        biologicalHazardCard.setStatus(true);
        biologicalHazardCard.setAuthor(user);
        biologicalHazardCard.setSym(sym);
        biologicalHazardCard.setMan(man);
        biologicalHazardCard.setSops(sops);
        biologicalHazardCard.setCat(cat);
        biologicalHazardCard.setDose(dose);
        biologicalHazardCard.setPeriod(period);
        biologicalHazardCard.setState(state);
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
