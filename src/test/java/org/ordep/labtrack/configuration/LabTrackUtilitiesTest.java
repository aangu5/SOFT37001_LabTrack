package org.ordep.labtrack.configuration;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LabTrackUtilitiesTest {

    @Test
    void formatDate() {
        LocalDateTime date = LocalDateTime.parse("2007-12-03T10:15:30");
        assertEquals("03/12/2007",LabTrackUtilities.formatDate(date));
    }

    @Test
    void formatDate_today() {
        LocalDateTime date = LocalDateTime.now();
        assertEquals("Today",LabTrackUtilities.formatDate(date));
    }

    @Test
    void formatDate_yesterday() {
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        assertEquals("Yesterday",LabTrackUtilities.formatDate(date));
    }
}