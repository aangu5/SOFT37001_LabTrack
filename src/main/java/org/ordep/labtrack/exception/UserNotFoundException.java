package org.ordep.labtrack.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID uuid) {
        super();
        log.error("User not found: {}", uuid);
    }
}
