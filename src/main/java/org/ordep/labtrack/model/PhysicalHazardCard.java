package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.MandatoryPictogram;
import org.ordep.labtrack.model.enums.PhysicalPictogram;

import jakarta.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PhysicalHazardCard extends Card {
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<PhysicalPictogram> symbols;
    private String hazards;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<MandatoryPictogram> mandatoryPictograms;
}
