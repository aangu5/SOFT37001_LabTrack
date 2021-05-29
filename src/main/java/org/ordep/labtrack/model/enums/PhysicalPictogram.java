package org.ordep.labtrack.model.enums;

public enum PhysicalPictogram {
    PHS01("Laser"),
    PHS02("Hot"),
    PHS03("Radio"),
    PHS04("Ice"),
    PHS05("Magnets"),
    PHS06("Breath a bit"),
    PHS07("Nuclear"),
    PHS08("Trip Hazard"),
    PHS09("Fall Hazard"),
    PHS10("Risk of Shock"),
    PHS11("Warning"),
    PHS12("Break up?!");

    private final String displayName;

    PhysicalPictogram(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
