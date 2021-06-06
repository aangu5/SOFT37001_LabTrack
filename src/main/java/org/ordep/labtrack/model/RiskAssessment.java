package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.ChemicalPictogram;
import org.ordep.labtrack.model.enums.FrequencyOfTask;
import org.ordep.labtrack.model.enums.Severity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RiskAssessment extends Assessment {

    private String reaction;
    @ElementCollection
    private List<ChemicalPictogram> pictograms;
    @OneToMany
    private List<HazardStatement> hazardStatements;
    @ElementCollection
    private List<String> risks;
    private String numberOfExposures;
    @Enumerated(EnumType.STRING)
    private FrequencyOfTask frequencyOfTask;
    @Enumerated(EnumType.STRING)
    private Severity severity;
    @Enumerated(EnumType.STRING)
    private Severity likelihood;
    private String riskRating;
    @OneToMany
    private List<PrecautionaryStatement> precautionaryStatements;
    @OneToMany
    private List<Sop> sops;
    private String signature;

    @ManyToMany
    private List<PhysicalHazardCard> physicalHazardCards;
    @ManyToMany
    private List<ChemicalHazardCard> chemicalHazardCards;
    @ManyToMany
    private List<BiologicalHazardCard> biologicalHazardCards;

}
