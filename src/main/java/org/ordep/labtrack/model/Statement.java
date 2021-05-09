package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class Statement {
    @Id
    private UUID statementId;
    private String statementName;
    private String statementState;
}
