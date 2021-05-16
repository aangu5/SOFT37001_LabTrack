package org.ordep.labtrack.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ordep.labtrack.configuration.LabTrackUtilities;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationData {
    @Pattern(regexp = LabTrackUtilities.EMAIL_REGEX)
    private String username;
    @Pattern(regexp = LabTrackUtilities.PASSWORD_REGEX)
    private String password;
    private String displayName;
}
