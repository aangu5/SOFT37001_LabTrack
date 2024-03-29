package org.ordep.labtrack.model;

import lombok.Data;
import org.ordep.labtrack.configuration.LabTrackUtilities;
import org.ordep.labtrack.model.enums.AssessmentState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class Assessment {
    @Id
    private UUID assessmentId;
    private String assessmentName;
    @OneToOne
    private LabTrackUser author;
    @OneToOne
    private LabTrackUser approver;
    private LocalDateTime dateCreated;
    private LocalDateTime dateApproved;
    @Enumerated(EnumType.STRING)
    private AssessmentState status;
    private String declinedReason;

    public String getFormattedCreatedDate() {
        if (this.getDateCreated() != null) {
            return LabTrackUtilities.formatDate(this.getDateCreated());
        }
        return null;
    }

    public String getFormattedApprovedDate() {
        if (this.getDateApproved() != null) {
            return LabTrackUtilities.formatDate(this.getDateApproved());
        }
        return null;
    }

    public String isActive() {
        return status.getDisplayName();
    }
}
