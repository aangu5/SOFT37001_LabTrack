package org.ordep.labtrack.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ordep.labtrack.model.enums.Role;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LabTrackUser {
    @Id
    private UUID userId;
    private String displayName;
    private String emailAddress;
    private boolean isLoggedIn;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

}