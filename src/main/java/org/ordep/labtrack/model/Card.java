package org.ordep.labtrack.model;

import lombok.Data;
import org.ordep.labtrack.configuration.LabTrackUtilities;
import org.ordep.labtrack.model.enums.PictogramType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class Card {
    @Id
    private UUID cardId;
    private String cardName;
    private String synonyms;
    @ElementCollection
    private List<PictogramType> pictograms;
    private boolean status;
    private LocalDateTime dateCreated;
    @ManyToOne
    private LabTrackUser author;

    public String getFormattedDate() {
        if (dateCreated != null) {
            return LabTrackUtilities.formatDate(dateCreated);
        }
        return null;
    }

    public String isActive() {
        return status ? "Active" : "Inactive";
    }
}
