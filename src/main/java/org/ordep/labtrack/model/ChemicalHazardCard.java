package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.SignalWord;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChemicalHazardCard extends Card {
    private String cas;
    @Enumerated(EnumType.STRING)
    private SignalWord signalWord;
    @OneToMany
    private List<HazardStatement> hazardStatements;
    @OneToMany
    private List<PrecautionaryStatement> precautionaryStatements;
}
