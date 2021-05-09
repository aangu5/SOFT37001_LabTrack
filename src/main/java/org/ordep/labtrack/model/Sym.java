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
public class Sym extends PhysicalHazardData {
    private String symName;
    private String symState;

    public Sym(UUID symId, String symName, String symState) {
        super(symId);
        this.symName = symName;
        this.symState = symState;
    }
}