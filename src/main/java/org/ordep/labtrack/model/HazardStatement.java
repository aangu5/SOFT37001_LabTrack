package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
public class HazardStatement extends Statement {

    public HazardStatement(UUID statementId, String statementName, String statementState) {
        super(statementId, statementName, statementState);
    }
}
