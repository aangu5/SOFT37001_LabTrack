package org.ordep.labtrack.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ordep.labtrack.model.BiologicalHazardCard;
import org.ordep.labtrack.model.ChemicalHazardCard;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.PhysicalHazardCard;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessmentDTO {
    private String assessmentName;
    private LabTrackUser author;
    private String reaction;

    private List<String> risks;
    private String numberOfExposures;
    private String frequencyOfTask;
    private String severity;
    private String likelihood;
    private String riskRating;

    private List<PhysicalHazardCard> physicalHazardCards;
    private List<ChemicalHazardCard> chemicalHazardCards;
    private List<BiologicalHazardCard> biologicalHazardCards;
}
