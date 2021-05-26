package org.ordep.labtrack.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class UserException extends RuntimeException {
    public UserException(UUID uuid) {
        super();
        log.error("User not found: {}", uuid);
    }

    public UserException(String message) {
        super();
        log.error("User Service error: {}", message);
    }
}
