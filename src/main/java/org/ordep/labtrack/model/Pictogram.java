package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ordep.labtrack.model.enums.PictogramType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pictogram {
    @Id
    private UUID pictogramId;
    private String pictogramName;
    @Enumerated(EnumType.STRING)
    private PictogramType image;
}
