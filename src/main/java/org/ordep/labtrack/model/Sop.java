package org.ordep.labtrack.model;

import lombok.*;

import javax.persistence.Entity;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sop extends PhysicalHazardData {
    private String sopName;
    private String sopState;

    public Sop(UUID dataId, String sopName, String sopState) {
        super(dataId);
        this.sopName = sopName;
        this.sopState = sopState;
    }
}