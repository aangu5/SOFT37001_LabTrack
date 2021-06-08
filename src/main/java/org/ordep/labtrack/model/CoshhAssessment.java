package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.HazardToHealth;
import org.ordep.labtrack.model.enums.Precaution;
import org.ordep.labtrack.model.enums.ProtectiveEquipment;
import org.ordep.labtrack.model.enums.RouteOfExposure;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CoshhAssessment extends Assessment {
    private String supplierName;
    private String cas;
    private String quantityOrdered;
    @ManyToMany
    private List<HazardStatement> hazardStatements;
    @ManyToMany
    private List<PrecautionaryStatement> precautionaryStatements;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<HazardToHealth> hazardToHealths;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<RouteOfExposure> routeOfExposures;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Precaution> precautions;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ProtectiveEquipment> protectiveEquipments;
    private String storageInformation;
    private String disposalMethod;
    private String actionInCaseOfSpillage;
    private String firePrecaution;
    private String fireAidAction;
    @ManyToMany
    private List<LabTrackUser> signedUsers;
}
