package org.ordep.labtrack.configuration;

import org.ordep.labtrack.model.CoshhAssessment;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.AssessmentState;
import org.ordep.labtrack.model.enums.Role;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LabTrackUtilities {

    private LabTrackUtilities() {

    }

    public static String formatDate(LocalDateTime dateTime) {
        String output;
        var now = LocalDateTime.now();
        var midnight = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);

        if (dateTime.isAfter(midnight.minusDays(1)) && dateTime.isBefore(midnight)) {
            output = "Yesterday";
        } else if (dateTime.isAfter(midnight) && dateTime.isBefore(midnight.plusDays(1))) {
            output = "Today";
        } else {
            output = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return output;
    }

    public static boolean isUserSenior(Role role1, Role role2) {
        int priority1 = role1.getPriority();
        int priority2 = role2.getPriority();

        return priority1 > priority2;

    }

    public static List<Role> getJuniorRoles(Role userRole){
        List<Role> rolesList = new ArrayList<>();
        for (Role role : Role.values()){
            if (role.getPriority() <= userRole.getPriority()) {
                rolesList.add(role);
            }
        }
        return rolesList;
    }

    public static Role getHighestRole(LabTrackUser user) {
        List<Role> roles = user.getRoles();

        if (roles.contains(Role.ADMIN)) {
            return Role.ADMIN;
        }
        else if (roles.contains(Role.LECTURER)){
            return Role.LECTURER;
        }
        else if (roles.contains(Role.STUDENT)){
            return Role.STUDENT;
        }
        else {
            return Role.USER;
        }
    }

    public static boolean canUserApprove(LabTrackUser user) {
        List<Role> roles = user.getRoles();

        return roles.contains(Role.ADMIN) || roles.contains(Role.LECTURER);
    }

    public static boolean hasUserSignedAssessment(CoshhAssessment assessment, LabTrackUser user) {
        return assessment.getSignedUsers().contains(user);
    }
}
