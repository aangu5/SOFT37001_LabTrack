package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Entity;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
public class PrecautionaryStatement extends Statement {
    public PrecautionaryStatement(UUID statementId, String statementName, String statementState) {
        super(statementId, statementName, statementState);
    }
}
