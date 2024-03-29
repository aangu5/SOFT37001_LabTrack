package org.ordep.labtrack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.*;
import org.ordep.labtrack.exception.CardNotFoundException;
import org.ordep.labtrack.model.*;
import org.ordep.labtrack.model.enums.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    private final UUID uuid1 = UUID.fromString("c8fdb523-9278-45f6-9cc3-d5b913c47275");
    private final UUID uuid2 = UUID.fromString("5d83af65-e06e-4e1b-8bdd-c2539426f700");
    private final UUID userID = UUID.fromString("702d1aab-fd01-400f-9d08-fb6485b0a773");
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ChemicalHazardCardRepository chemicalHazardCardRepository;
    @Mock
    private PhysicalHazardCardRepository physicalHazardCardRepository;
    @Mock
    private BiologicalHazardCardRepository biologicalHazardCardRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Chemistry Hazard Card

    @Test
    void newChemicalHazardCard() {
        List<ChemicalPictogram> pictograms = Collections.singletonList(ChemicalPictogram.GHS01);

        HazardStatement hazardStatement = new HazardStatement(uuid1, "name", "state");
        List<HazardStatement> hazardStatements = Collections.singletonList(hazardStatement);

        PrecautionaryStatement precautionaryStatement = new PrecautionaryStatement(uuid2, "name",
                "state");
        List<PrecautionaryStatement> precautionaryStatements = Collections.singletonList(precautionaryStatement);

        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", Collections.singletonList(Role.USER));
        when(userService.getCurrentUser()).thenReturn(user);

        var input = new ChemicalHazardCard();
        input.setCardName("name");
        input.setCas("cas");
        input.setPictograms(pictograms);
        input.setHazardStatements(hazardStatements);
        input.setPrecautionaryStatements(precautionaryStatements);
        input.setSynonyms("synonyms");
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

        when(chemicalHazardCardRepository.findChemicalHazardCardsByAuthor(any(), any())).thenReturn(cards);

        List<ChemicalHazardCard> output = cardService.findAllChemicalHazardCardsForUser(userID, 1);

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
        List<PhysicalPictogram> pictograms = Collections.singletonList(PhysicalPictogram.PHS01);

        List<MandatoryPictogram> men = Collections.singletonList(MandatoryPictogram.MHS01);

        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", Collections.singletonList(Role.USER));
        when(userService.getCurrentUser()).thenReturn(user);

        var input = new PhysicalHazardCard();
        input.setCardName("name");
        input.setSymbols(pictograms);
        input.setHazards("hazs");
        input.setMandatoryPictograms(men);
        input.setAuthor(user);

        PhysicalHazardCard card = cardService.newPhysicalHazardCard(input);

        verify(physicalHazardCardRepository, times(1)).save(any());
        assertEquals("name", card.getCardName());
        assertEquals(user, card.getAuthor());
        assertEquals(pictograms, card.getSymbols());
        assertEquals("hazs", card.getHazards());
        assertEquals(men, card.getMandatoryPictograms());

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

        LabTrackUser user = new LabTrackUser(userID, "display name", "email@mail.com", Collections.singletonList(Role.USER));
        when(userService.getCurrentUser()).thenReturn(user);

        var input = new BiologicalHazardCard();
        input.setCardName("name");
        input.setHazardous(true);
        input.setAuthor(user);

        BiologicalHazardCard card = cardService.newBiologicalHazardCard(input);

        verify(biologicalHazardCardRepository, times(1)).save(any());
        assertEquals("name", card.getCardName());
        assertEquals(user, card.getAuthor());
        assertTrue(card.isHazardous());
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

        when(chemicalHazardCardRepository.findAllByOrderByDateCreatedDesc()).thenReturn(chemicalCards);
        when(physicalHazardCardRepository.findAllByOrderByDateCreatedDesc()).thenReturn(physicalCards);
        when(biologicalHazardCardRepository.findAllByOrderByDateCreatedDesc()).thenReturn(biologicalCards);

        expected.sort(Comparator.comparing(Card::getDateCreated).reversed());

        List<Card> actual = cardService.getAllCards();

        assertEquals(expected, actual);
    }

    @Test
    void searchCardName() throws IOException {
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

        when(chemicalHazardCardRepository.findByCardNameContainsIgnoreCase(anyString())).thenReturn(chemicalCards);
        when(physicalHazardCardRepository.findByCardNameContainsIgnoreCase(anyString())).thenReturn(physicalCards);
        when(biologicalHazardCardRepository.findByCardNameContainsIgnoreCase(anyString())).thenReturn(biologicalCards);

        expected.sort(Comparator.comparing(Card::getDateCreated).reversed());

        List<Card> actual = cardService.searchCardName("search term");

        assertEquals(expected, actual);
    }

    @Test
    void searchCardAuthor() throws IOException {
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

        when(chemicalHazardCardRepository.findByAuthor_DisplayNameContainsIgnoreCase(anyString())).thenReturn(chemicalCards);
        when(physicalHazardCardRepository.findByAuthor_DisplayNameContainsIgnoreCase(anyString())).thenReturn(physicalCards);
        when(biologicalHazardCardRepository.findByAuthor_DisplayNameContainsIgnoreCase(anyString())).thenReturn(biologicalCards);

        expected.sort(Comparator.comparing(Card::getDateCreated).reversed());

        List<Card> actual = cardService.searchCardAuthor("search term");

        assertEquals(expected, actual);
    }
}