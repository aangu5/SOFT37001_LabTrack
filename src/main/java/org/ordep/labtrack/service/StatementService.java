package org.ordep.labtrack.service;

import lombok.extern.slf4j.Slf4j;
import org.ordep.labtrack.data.HazardStatementRepository;
import org.ordep.labtrack.data.PrecautionaryStatementRepository;
import org.ordep.labtrack.model.HazardStatement;
import org.ordep.labtrack.model.PrecautionaryStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class StatementService {

    @Autowired
    private HazardStatementRepository hazardStatementRepository;
    @Autowired
    private PrecautionaryStatementRepository precautionaryStatementRepository;

    public List<HazardStatement> findHazardStatements(List<UUID> statementIds) {
        return hazardStatementRepository.findByStatementIdIn(statementIds);
    }

    public List<PrecautionaryStatement> findPrecautionaryStatements(List<UUID> statementIds) {
        return precautionaryStatementRepository.findByStatementIdIn(statementIds);
    }

    public List<HazardStatement> getAllHazardStatements() {
        List<HazardStatement> output = new ArrayList<>();
        hazardStatementRepository.findAll().forEach(output::add);
        return output;
    }

    public List<PrecautionaryStatement> getAllPrecautionaryStatements() {
        List<PrecautionaryStatement> output = new ArrayList<>();
        precautionaryStatementRepository.findAll().forEach(output::add);
        return output;
    }
}
