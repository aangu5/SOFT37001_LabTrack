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
public class Man extends PhysicalHazardData {
    private String manName;
    private String manState;

    public Man(UUID manID, String manName, String manState) {
        super(manID);
        this.manName = manName;
        this.manState = manState;
    }
}