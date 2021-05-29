package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.ordep.labtrack.configuration.LabTrackUtilities;
import org.ordep.labtrack.model.enums.ChemicalPictogram;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
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
    private String frequencyOfTask;
    private String severity;
    private String likelihood;
    private String riskRating;
    @OneToMany
    private List<PrecautionaryStatement> precautionaryStatements;
    @OneToMany
    private List<Sop> sops;
    private String signature;

    @OneToMany
    private List<PhysicalHazardCard> physicalHazardCards;
    @OneToMany
    private List<ChemicalHazardCard> chemicalHazardCards;
    @OneToMany
    private List<BiologicalHazardCard> biologicalHazardCards;

}
