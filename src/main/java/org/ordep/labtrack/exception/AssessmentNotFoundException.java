package org.ordep.labtrack.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class AssessmentNotFoundException extends RuntimeException {
    public AssessmentNotFoundException(UUID assessmentId) {
        super();
        log.error("Assessment not found for ID: {}", assessmentId);
    }
}
