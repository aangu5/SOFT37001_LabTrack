package org.ordep.labtrack.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(UUID cardId) {
        super();
        log.error("Card not found for ID: {}", cardId);
    }
}
