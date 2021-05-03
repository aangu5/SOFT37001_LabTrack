package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChemistryHazardCard extends Card {
    private String cas;
    @OneToMany
    private List<Pictogram> pictograms;
    @OneToMany
    private List<HazardStatement> hazardStatements;
    @OneToMany
    private List<PrecautionaryStatement> precautionaryStatements;
}
