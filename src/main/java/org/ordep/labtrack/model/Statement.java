package org.ordep.labtrack.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class Statement {
    @Id
    private UUID statementId;
    private String statementName;
    private String statementState;
}
