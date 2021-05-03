package org.ordep.labtrack.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class PhysicalHazardData {
    @Id
    private UUID dataId;
}
