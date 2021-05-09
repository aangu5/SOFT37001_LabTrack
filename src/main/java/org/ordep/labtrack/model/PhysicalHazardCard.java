package org.ordep.labtrack.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PhysicalHazardCard extends Card {
    @OneToMany
    private List<Sym> syms;
    @OneToMany
    private List<Haz> hazs;
    @OneToMany
    private List<Man> men;
    @OneToMany
    private List<Sop> sops;
}
