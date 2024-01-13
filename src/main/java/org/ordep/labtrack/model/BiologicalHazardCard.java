package org.ordep.labtrack.model;

import lombok.*;
import org.ordep.labtrack.model.enums.BioSafetyLevel;

import jakarta.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BiologicalHazardCard extends Card {
    private boolean hazardous;
    private BioSafetyLevel bioSafetyLevel;
}
