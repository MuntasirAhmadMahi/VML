package com.mam.model;

import java.time.LocalDate;
import java.util.UUID;

public class MileageTask extends Task {
    private int dueAtOdometer;
    private int savedDueAtOdometer;

    public MileageTask(UUID id, TaskKind type, String name, String note, int dueAtOdometer, int savedDueAtOdometer) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.note = note;
        this.dueAtOdometer = dueAtOdometer;
        this.savedDueAtOdometer = savedDueAtOdometer;
    }

    public static MileageTask create(TaskKind type, String note, int dueAtOdometer) {
        return new MileageTask(UUID.ofEpochMillis(System.currentTimeMillis()), type, type.toString(), note, dueAtOdometer, 0);
    }

    public static MileageTask create(String name, String note, int dueAtOdometer) {
        return new MileageTask(UUID.ofEpochMillis(System.currentTimeMillis()), TaskKind.CUSTOM, name, note, dueAtOdometer, 0);
    }

    public int getDueAtOdometer() {
        return dueAtOdometer;
    }

    public int getSavedDueAtOdometer() {
        return savedDueAtOdometer;
    }

    @Override
    public boolean isDue() {
        return dueAtOdometer != Integer.MAX_VALUE;
    }

    @Override
    public DueInfo getDueInfo(LocalDate today, int currentOdometer) {
        if (!isDue()) {
            return new DueInfo(DueStatus.COMPLETED, "Completed");
        }
        int v = dueAtOdometer - currentOdometer;
        if (v == 0) {
            return new DueInfo(DueStatus.DUE_NOW, "Odometer Reached");
        } else if (v < 0) {
            return new DueInfo(DueStatus.OVERDUE, "Overdue by " + (-v) + " KM");
        } else if (v <= 50) {
            return new DueInfo(DueStatus.DUE_SOON, "Due soon: " + v + " KM remaining");
        } else {
            return new DueInfo(DueStatus.UPCOMING, "Due in " + v + " KM");
        }
    }

    @Override
    public void resetCounter() {
        savedDueAtOdometer = dueAtOdometer;
        dueAtOdometer = Integer.MAX_VALUE;
    }
}
