package org.ordep.labtrack.model;

import lombok.Data;
import org.ordep.labtrack.configuration.LabTrackUtilities;

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
    @ElementCollection
    private List<String> synonyms;
    @OneToMany
    private List<Pictogram> pictograms;
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
}
