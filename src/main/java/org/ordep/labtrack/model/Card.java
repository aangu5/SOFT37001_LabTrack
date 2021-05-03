package org.ordep.labtrack.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class Card {
    @Id
    private UUID cardId;
    private String cardName;
}
