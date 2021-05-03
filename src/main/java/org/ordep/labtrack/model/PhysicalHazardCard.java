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
