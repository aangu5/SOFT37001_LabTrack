package org.ordep.labtrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ordep.labtrack.data.HazardStatementRepository;
import org.ordep.labtrack.data.PrecautionaryStatementRepository;
import org.junit.jupiter.api.Test;
import org.ordep.labtrack.model.HazardStatement;
import org.ordep.labtrack.model.PrecautionaryStatement;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {
    @InjectMocks
    private StatementService statementService;
    @Mock
    private HazardStatementRepository hazardStatementRepository;
    @Mock
    private PrecautionaryStatementRepository precautionaryStatementRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllHazardStatementsTest() throws Exception {
        HazardStatement hazardStatement = objectMapper.readValue(new ClassPathResource("/json/HazardStatement.json").getInputStream(), HazardStatement.class);
        Mockito.when(hazardStatementRepository.findAll()).thenReturn(Collections.singletonList(hazardStatement));

        assertEquals(Collections.singletonList(hazardStatement), statementService.getAllHazardStatements());
    }

    @Test
    void getAllPrecautionaryStatementsTest() throws Exception {
        PrecautionaryStatement precautionaryStatement = objectMapper.readValue(new ClassPathResource("/json/PrecautionaryStatement.json").getInputStream(), PrecautionaryStatement.class);
        Mockito.when(precautionaryStatementRepository.findAll()).thenReturn(Collections.singletonList(precautionaryStatement));

        assertEquals(Collections.singletonList(precautionaryStatement), statementService.getAllPrecautionaryStatements());
    }

    @Test
    void findHazardStatements() throws Exception {
        HazardStatement hazardStatement = objectMapper.readValue(new ClassPathResource("/json/HazardStatement.json").getInputStream(), HazardStatement.class);
        Mockito.when(hazardStatementRepository.findByStatementIdIn(Mockito.anyList())).thenReturn(Collections.singletonList(hazardStatement));

        List<UUID> ids = Collections.singletonList(UUID.fromString("c8fdb523-9278-45f6-9cc3-d5b913c47275"));

        assertEquals(Collections.singletonList(hazardStatement), statementService.findHazardStatements(ids));
    }

    @Test
    void findPrecautionaryStatements() throws Exception {
        PrecautionaryStatement precautionaryStatement = objectMapper.readValue(new ClassPathResource("/json/PrecautionaryStatement.json").getInputStream(), PrecautionaryStatement.class);
        Mockito.when(precautionaryStatementRepository.findByStatementIdIn(Mockito.anyList())).thenReturn(Collections.singletonList(precautionaryStatement));

        List<UUID> ids = Collections.singletonList(UUID.fromString("c8fdb523-9278-45f6-9cc3-d5b913c47275"));

        assertEquals(Collections.singletonList(precautionaryStatement), statementService.findPrecautionaryStatements(ids));
    }
}
