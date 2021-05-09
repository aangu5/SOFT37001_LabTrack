package org.ordep.labtrack.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LabTrackUtilities {

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
}
