package org.ordep.labtrack.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    STUDENT,
    LECTURER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
