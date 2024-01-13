package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.FrequencyOfTask;
import org.ordep.labtrack.model.enums.Likelihood;
import org.ordep.labtrack.model.enums.Severity;

import jakarta.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RiskAssessment extends Assessment {

    private String reaction;
    private String risks;
    private String numberOfExposures;
    @Enumerated(EnumType.STRING)
    private FrequencyOfTask frequencyOfTask;
    @Enumerated(EnumType.STRING)
    private Severity severity;
    @Enumerated(EnumType.STRING)
    private Likelihood likelihood;
    private String riskRating;
    private String signature;

    @ManyToMany
    private List<PhysicalHazardCard> physicalHazardCards;
    @ManyToMany
    private List<ChemicalHazardCard> chemicalHazardCards;
    @ManyToMany
    private List<BiologicalHazardCard> biologicalHazardCards;

}
