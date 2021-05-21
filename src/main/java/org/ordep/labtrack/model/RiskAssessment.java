package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RiskAssessment {
    @Id
    private UUID assessmentId;
    private String assessmentName;
    @OneToOne
    private LabTrackUser author;
    @OneToOne
    private LabTrackUser approver;
    private String reaction;
    @OneToMany
    private List<Pictogram> pictograms;
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
    private LocalDateTime dateSigned;
    private boolean approved;

}
