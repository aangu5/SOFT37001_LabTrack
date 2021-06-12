package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.ChemicalPictogram;
import org.ordep.labtrack.model.enums.SignalWord;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChemicalHazardCard extends Card {

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ChemicalPictogram> pictograms;
    private String cas;
    @Enumerated(EnumType.STRING)
    private SignalWord signalWord;
    @ManyToMany
    private List<HazardStatement> hazardStatements;
    @ManyToMany
    private List<PrecautionaryStatement> precautionaryStatements;
}
