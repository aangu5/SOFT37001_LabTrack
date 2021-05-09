package org.ordep.labtrack.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BiologicalHazardCard extends Card {
    @OneToOne
    private Sym sym;
    private String cat;
    private String dose;
    private String period;
    private String state;
    @OneToOne
    private Man man;
    @OneToMany
    private List<Sop> sops;
}
