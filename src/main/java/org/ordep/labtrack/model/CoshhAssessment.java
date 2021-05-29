package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.HazardToHealth;
import org.ordep.labtrack.model.enums.Precaution;
import org.ordep.labtrack.model.enums.ProtectiveEquipment;
import org.ordep.labtrack.model.enums.RouteOfExposure;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
    @OneToMany
    private List<HazardStatement> hazardStatements;
    @OneToMany
    private List<PrecautionaryStatement> precautionaryStatements;
    @ElementCollection
    private List<HazardToHealth> hazardToHealths;
    @ElementCollection
    private List<RouteOfExposure> routeOfExposures;
    @ElementCollection
    private List<Precaution> precautions;
    @ElementCollection
    private List<ProtectiveEquipment> protectiveEquipments;
    private String storageInformation;
    private String disposalMethod;
    private String actionInCaseOfSpillage;
    private String firePrecaution;
    private String fireAidAction;
    @OneToMany
    private List<LabTrackUser> signedUsers;
}
