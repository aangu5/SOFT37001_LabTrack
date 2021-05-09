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
public class Haz extends PhysicalHazardData {
    private String hazName;
    private String hazState;

    public Haz(UUID id, String hazName, String hazState) {
        super(id);
        this.hazName = hazName;
        this.hazState = hazState;
    }
}