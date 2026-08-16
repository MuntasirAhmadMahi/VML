package com.mam.model;

public enum TaskKind {
    BATTERY_REPLACEMENT,
    BRAKE_PAD_REPLACEMENT,
    ENGINE_INSPECTION,
    ENGINE_OIL_CHANGE,
    EXHAUST_SYSTEM_INSPECTION,
    SPARK_PLUG_REPLACEMENT,
    SUSPENSION_INSPECTION,
    TRANSMISSION_OIL_CHANGE,
    TIRE_REPLACEMENT,
    WHEEL_ALIGNMENT_AND_BALANCING,
    CUSTOM;

    @Override
    public String toString() {
        String[] parts = this.name().split("_");
        StringBuilder name = new StringBuilder();
        for (String part : parts) {
            name.append(part.charAt(0));
            name.append(part.substring(1).toLowerCase());
            name.append(" ");
        }
        name.deleteCharAt(name.length() - 1);
        return name.toString();
    }
}
