package org.ordep.labtrack.model;

import lombok.Data;
import org.ordep.labtrack.configuration.LabTrackUtilities;
import org.ordep.labtrack.model.enums.CardType;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class Card {
    @Id
    private UUID cardId;
    private String cardName;
    private String synonyms;
    private LocalDateTime dateCreated;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @ManyToOne
    private LabTrackUser author;

    public String getFormattedDate() {
        if (dateCreated != null) {
            return LabTrackUtilities.formatDate(dateCreated);
        }
        return null;
    }

    public String getType() {
        if (cardType != null) {
            return cardType.getDisplayName();
        }
        return "Unknown Type";
    }
}
