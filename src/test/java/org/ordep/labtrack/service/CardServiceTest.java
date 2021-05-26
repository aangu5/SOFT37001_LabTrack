package org.ordep.labtrack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.*;
import org.ordep.labtrack.exception.CardNotFoundException;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.PictogramType;
import org.ordep.labtrack.model.enums.SignalWord;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    private final UUID pictogramID = UUID.fromString("95943ef4-ca07-4c7c-b9f8-67b170e6e6bc");
    private final UUID uuid1 = UUID.fromString("c8fdb523-9278-45f6-9cc3-d5b913c47275");
    private final UUID uuid2 = UUID.fromString("5d83af65-e06e-4e1b-8bdd-c2539426f700");
    private final UUID uuid3 = UUID.fromString("360bcc84-16af-4912-9453-b801251150ca");
    private final UUID uuid4 = UUID.fromString("9e2f5c23-6dd6-4870-9382-48ef5c6e296c");
    private final UUID userID = UUID.fromString("702d1aab-fd01-400f-9d08-fb6485b0a773");
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ChemicalHazardCardRepository chemicalHazardCardRepository;
    @Mock
    private PhysicalHazardCardRepository physicalHazardCardRepository;
    @Mock
    private BiologicalHazardCardRepository biologicalHazardCardRepository;
    @Mock
    private PictogramRepository pictogramRepository;
    @Mock
    private StatementService statementService;
    @Mock
    private SymRepository symRepository;
    @Mock
    private HazRepository hazRepository;
    @Mock
    private ManRepository manRepository;
    @Mock
    private SopRepository sopRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
    }

    // Chemistry Hazard Card

    @Test
    void newChemicalHazardCard() throws JsonProcessingException {
        List<PictogramType> pictograms = Collections.singletonList(PictogramType.GHS01);

        HazardStatement hazardStatement = new HazardStatement(uuid1, "name", "state");
        List<HazardStatement> hazardStatements = Collections.singletonList(hazardStatement);

        PrecautionaryStatement precautionaryStatement = new PrecautionaryStatement(uuid2, "name",
                "state");
        List<PrecautionaryStatement> precautionaryStatements = Collections.singletonList(precautionaryStatement);

        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", false);
        when(userService.getCurrentUser()).thenReturn(user);

        List<String> synonyms = Arrays.asList("synonym 1", "synonym 2");

        var input = new ChemicalHazardCard();
        input.setCardName("name");
        input.setCas("cas");
        input.setPictograms(pictograms);
        input.setHazardStatements(hazardStatements);
        input.setPrecautionaryStatements(precautionaryStatements);
        input.setSynonyms(synonyms);
        input.setSignalWord(SignalWord.DANGER);

        ChemicalHazardCard card = cardService.newChemicalHazardCard(input);

        verify(chemicalHazardCardRepository, times(1)).save(any());
        assertEquals("name", card.getCardName());
        assertEquals("cas", card.getCas());
        assertEquals(pictograms, card.getPictograms());
        assertEquals(hazardStatements, card.getHazardStatements());
        assertEquals(precautionaryStatements, card.getPrecautionaryStatements());
        assertEquals(user, card.getAuthor());
    }

    @Test
    void findAllChemicalHazardCards() throws IOException {

        List<ChemicalHazardCard> cards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                "/json/ChemicalHazardCard.json").getInputStream(), ChemicalHazardCard.class));

        when(chemicalHazardCardRepository.findAll()).thenReturn(cards);

        List<ChemicalHazardCard> output = cardService.findAllChemicalHazardCards();

        assertEquals(cards, output);

    }

    @Test
    void findAllChemicalHazardCardsForUser() throws IOException {

        List<ChemicalHazardCard> cards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                        "/json/ChemicalHazardCard.json").getInputStream(),
                ChemicalHazardCard.class));

        when(chemicalHazardCardRepository.findChemicalHazardCardsByAuthor(any())).thenReturn(cards);

        List<ChemicalHazardCard> output = cardService.findAllChemicalHazardCardsForUser(userID);

        assertEquals(cards, output);
    }

    @Test
    void findOneChemicalHazardCard_success() throws IOException {
        ChemicalHazardCard card =
                objectMapper.readValue(new ClassPathResource("/json/ChemicalHazardCard.json").getInputStream(),
                        ChemicalHazardCard.class);

        when(chemicalHazardCardRepository.findById(any())).thenReturn(Optional.of(card));

        ChemicalHazardCard output = cardService.findOneChemicalHazardCard(UUID.fromString("e97ccd78-a05e-4b34-9318" +
                "-3bfdbe63ade9"));

        assertEquals(card, output);
    }

    @Test
    void findOneChemicalHazardCard_failure() {

        UUID cardID = UUID.fromString("e97ccd78-a05e-4b34-9318-3bfdbe63ade9");

        assertThrows(CardNotFoundException.class, () -> cardService.findOneChemicalHazardCard(cardID));
    }

    // Physical Hazard Cards

    @Test
    void newPhysicalHazardCard() {
        List<PictogramType> pictograms = Collections.singletonList(PictogramType.GHS01);

        Sym sym = new Sym(uuid1, "name", "state");
        List<Sym> syms = Collections.singletonList(sym);

        Haz haz = new Haz(uuid2, "name", "state");
        List<Haz> hazs = Collections.singletonList(haz);

        Man man = new Man(uuid3, "name", "state");
        List<Man> men = Collections.singletonList(man);

        Sop sop = new Sop(uuid4, "name", "state");
        List<Sop> sops = Collections.singletonList(sop);

        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", false);
        when(userService.getCurrentUser()).thenReturn(user);

        var input = new PhysicalHazardCard();
        input.setCardName("name");
        input.setPictograms(pictograms);
        input.setSyms(syms);
        input.setHazs(hazs);
        input.setMen(men);
        input.setSops(sops);
        input.setAuthor(user);

        PhysicalHazardCard card = cardService.newPhysicalHazardCard(input);

        verify(physicalHazardCardRepository, times(1)).save(any());
        assertEquals("name", card.getCardName());
        assertEquals(pictograms, card.getPictograms());
        assertEquals(user, card.getAuthor());
        assertEquals(syms, card.getSyms());
        assertEquals(hazs, card.getHazs());
        assertEquals(men, card.getMen());
        assertEquals(sops, card.getSops());

    }

    @Test
    void findAllPhysicalHazardCards() throws IOException {
        List<PhysicalHazardCard> cards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                "/json/PhysicalHazardCard.json").getInputStream(), PhysicalHazardCard.class));

        when(physicalHazardCardRepository.findAll()).thenReturn(cards);

        List<PhysicalHazardCard> output = cardService.findAllPhysicalHazardCards();

        assertEquals(cards, output);
    }

    @Test
    void findAllPhysicalHazardCardsForUser() throws IOException {
        List<PhysicalHazardCard> cards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                        "/json/PhysicalHazardCard.json").getInputStream(),
                PhysicalHazardCard.class));

        when(physicalHazardCardRepository.findPhysicalHazardCardsByAuthor(any())).thenReturn(cards);

        List<PhysicalHazardCard> output = cardService.findAllPhysicalHazardCardsForUser(userID);

        assertEquals(cards, output);

    }

    @Test
    void findOnePhysicalHazardCard_success() throws IOException {
        PhysicalHazardCard card =
                objectMapper.readValue(new ClassPathResource("/json/PhysicalHazardCard.json").getInputStream(),
                        PhysicalHazardCard.class);

        when(physicalHazardCardRepository.findById(any())).thenReturn(Optional.of(card));

        PhysicalHazardCard output = cardService.findOnePhysicalHazardCard(UUID.fromString("0abbd4da-c5c5-425f-a530" +
                "-295c2f06d12c"));

        assertEquals(card, output);
    }

    @Test
    void findOnePhysicalHazardCard_failure() {

        UUID cardID = UUID.fromString("e97ccd78-a05e-4b34-9318-3bfdbe63ade9");

        assertThrows(CardNotFoundException.class, () -> cardService.findOnePhysicalHazardCard(cardID));
    }

    // Biological Hazard Cards

    @Test
    void newBiologicalHazardCard() throws JsonProcessingException {
        List<PictogramType> pictograms = Collections.singletonList(PictogramType.GHS01);

        Sym sym = new Sym(uuid1, "name", "state");

        Man man = new Man(uuid2, "name", "state");

        Sop sop = new Sop(uuid3, "name", "state");
        List<Sop> sops = Collections.singletonList(sop);

        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", false);
        when(userService.getCurrentUser()).thenReturn(user);

        var input = new BiologicalHazardCard();
        input.setCardName("name");
        input.setPictograms(pictograms);
        input.setSym(sym);
        input.setMan(man);
        input.setSops(sops);
        input.setAuthor(user);

        BiologicalHazardCard card = cardService.newBiologicalHazardCard(input);

        verify(biologicalHazardCardRepository, times(1)).save(any());
        assertEquals("name", card.getCardName());
        assertEquals(pictograms, card.getPictograms());
        assertEquals(user, card.getAuthor());
        assertEquals(sym, card.getSym());
        assertEquals(man, card.getMan());
        assertEquals(sops, card.getSops());
        System.out.println(objectMapper.writeValueAsString(card));
    }

    @Test
    void findAllBiologicalHazardCards() throws IOException {
        List<BiologicalHazardCard> cards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                "/json/BiologicalHazardCard.json").getInputStream(), BiologicalHazardCard.class));

        when(biologicalHazardCardRepository.findAll()).thenReturn(cards);

        List<BiologicalHazardCard> output = cardService.findAllBiologicalHazardCards();

        assertEquals(cards, output);
    }

    @Test
    void findAllBiologicalHazardCardsForUser() throws IOException {
        List<BiologicalHazardCard> cards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                        "/json/BiologicalHazardCard.json").getInputStream(),
                BiologicalHazardCard.class));

        when(biologicalHazardCardRepository.findBiologicalHazardCardsByAuthor(any())).thenReturn(cards);

        List<BiologicalHazardCard> output = cardService.findAllBiologicalHazardCardsForUser(userID);

        assertEquals(cards, output);
    }

    @Test
    void findOneBiologicalHazardCard_success() throws IOException {

        BiologicalHazardCard card =
                objectMapper.readValue(new ClassPathResource("/json/BiologicalHazardCard.json").getInputStream(),
                        BiologicalHazardCard.class);

        when(biologicalHazardCardRepository.findById(any())).thenReturn(Optional.of(card));

        BiologicalHazardCard output = cardService.findOneBiologicalHazardCard(UUID.fromString("0abbd4da-c5c5-425f" +
                "-a530-295c2f06d12c"));

        assertEquals(card, output);
    }

    @Test
    void findOneBiologicalHazardCard_failure() {

        UUID cardID = UUID.fromString("e97ccd78-a05e-4b34-9318-3bfdbe63ade9");

        assertThrows(CardNotFoundException.class, () -> cardService.findOneBiologicalHazardCard(cardID));
    }

    // Other Methods

    @Test
    void getAllCards() throws IOException {
        List<ChemicalHazardCard> chemicalCards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                "/json/ChemicalHazardCard.json").getInputStream(), ChemicalHazardCard.class));
        List<PhysicalHazardCard> physicalCards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                "/json/PhysicalHazardCard.json").getInputStream(), PhysicalHazardCard.class));
        List<BiologicalHazardCard> biologicalCards = Collections.singletonList(objectMapper.readValue(new ClassPathResource(
                "/json/BiologicalHazardCard.json").getInputStream(), BiologicalHazardCard.class));

        List<Card> expected = new ArrayList<>();
        expected.addAll(chemicalCards);
        expected.addAll(physicalCards);
        expected.addAll(biologicalCards);

        when(chemicalHazardCardRepository.findAll()).thenReturn(chemicalCards);
        when(physicalHazardCardRepository.findAll()).thenReturn(physicalCards);
        when(biologicalHazardCardRepository.findAll()).thenReturn(biologicalCards);

        List<Card> actual = cardService.getAllCards();

        assertEquals(expected, actual);
    }
}