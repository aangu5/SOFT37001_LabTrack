package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class PhysicalHazardData {
    @Id
    private UUID dataId;
}
