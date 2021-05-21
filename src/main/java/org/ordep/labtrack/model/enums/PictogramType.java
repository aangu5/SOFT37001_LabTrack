package org.ordep.labtrack.model.enums;

public enum PictogramType {
    GHS01("Explosive"),
    GHS02("Flammable"),
    GHS03("Oxidising"),
    GHS04("Fl4mmable"),
    GHS05("Fl5mmable"),
    GHS06("Fl6mmable"),
    GHS07("Fl7mmable"),
    GHS08("Fl8mmable"),
    GHS09("Fl9mmable");

    private final String displayName;

    PictogramType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
