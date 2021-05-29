package org.ordep.labtrack.model.enums;

public enum RouteOfExposure {
    INHALATION("Inhalation"),
    INGESTION("Ingestion"),
    SKIN_ABSORPTION("Skin Absorption"),
    EYE_CONTACT("Eye Contact"),
    SKIN_CONTACT("Skin Contact"),
    INJECTION_SHARPS("Injection/Sharps");

    private final String displayName;

    RouteOfExposure(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
