package org.ordep.labtrack.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER("User", 0),
    STUDENT("Student", 1),
    LECTURER("Lecturer", 2),
    ADMIN("Admin", 3);

    private final String displayName;
    private final int priority;

    Role(String displayName, int priority) {
        this.displayName = displayName;
        this.priority = priority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}
