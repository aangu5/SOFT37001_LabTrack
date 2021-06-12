package org.ordep.labtrack.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ordep.labtrack.model.CoshhAssessment;
import org.ordep.labtrack.model.LabTrackUser;
import org.ordep.labtrack.model.enums.Role;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class LabTrackUtilitiesTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LabTrackUser user;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        user = objectMapper.readValue(new ClassPathResource("/json/LabTrackUser.json").getInputStream(), LabTrackUser.class);
    }

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

    @Test
    void getHighestRole_admin() {

        assertEquals(Role.ADMIN, LabTrackUtilities.getHighestRole(user));
    }

    @Test
    void getHighestRole_lecturer() {

        user.setRoles(Collections.singletonList(Role.LECTURER));

        assertEquals(Role.LECTURER, LabTrackUtilities.getHighestRole(user));
    }

    @Test
    void getHighestRole_student() {

        user.setRoles(Collections.singletonList(Role.STUDENT));

        assertEquals(Role.STUDENT, LabTrackUtilities.getHighestRole(user));
    }

    @Test
    void getHighestRole_user() {

        user.setRoles(Collections.singletonList(Role.USER));

        assertEquals(Role.USER, LabTrackUtilities.getHighestRole(user));
    }

    @Test
    void canUserApprove_true() {

        assertTrue(LabTrackUtilities.canUserApprove(user));
    }

    @Test
    void canUserApprove_false() {

        user.setRoles(Collections.singletonList(Role.USER));

        assertFalse(LabTrackUtilities.canUserApprove(user));
    }


    @Test
    void hasUserSignedAssessment() throws IOException {

        CoshhAssessment coshhAssessment = objectMapper.readValue(new ClassPathResource("/json/CoshhAssessment.json").getInputStream(), CoshhAssessment.class);

        assertTrue(LabTrackUtilities.hasUserSignedAssessment(coshhAssessment, user));

        user.setEmailAddress("test@gmail.com");

        assertFalse(LabTrackUtilities.hasUserSignedAssessment(coshhAssessment, user));

    }
}