package com.mam.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class TimeTask extends Task {
    private LocalDate dueDate;
    private LocalDate savedDate;

    public TimeTask(UUID id, TaskKind type, String name, String note, LocalDate dueDate, LocalDate savedDate) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.note = note;
        this.dueDate = dueDate;
        this.savedDate = savedDate;
    }

    public static TimeTask create(TaskKind type, String note, LocalDate dueDate) {
        return new TimeTask(UUID.ofEpochMillis(System.currentTimeMillis()), type, type.toString(), note, dueDate, null);
    }

    public static TimeTask create(String name, String note, LocalDate dueDate) {
        return new TimeTask(UUID.ofEpochMillis(System.currentTimeMillis()), TaskKind.CUSTOM, name, note, dueDate, null);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setSavedDate(LocalDate savedDate) {
        this.savedDate = savedDate;
    }

    public LocalDate getSavedDate() {
        return savedDate;
    }

    @Override
    public boolean isDue() { // if equal then it is completed
        return !dueDate.equals(LocalDate.MAX);
    }

    @Override
    public DueInfo getDueInfo(LocalDate today, int currentOdometer) {
        if (!isDue()) {
            return new DueInfo(DueStatus.COMPLETED, "Completed");
        }
        long v = ChronoUnit.DAYS.between(today, dueDate);
        if (v == 1) {
            return new DueInfo(DueStatus.DUE_SOON, "Due tomorrow");
        }
        if (v == 0) {
            return new DueInfo(DueStatus.DUE_NOW, "Due today");
        } else if (v < 0) {
            return new DueInfo(DueStatus.OVERDUE, "Overdue by " + (-v) + " day" + (-v == 1 ? "" : "s"));
        } else if (v <= 3) {
            return new DueInfo(DueStatus.DUE_SOON, "Due soon: " + v + " days remaining");
        } else {
            return new DueInfo(DueStatus.UPCOMING, "Due in " + v + " days");
        }
    }

    @Override
    public void resetCounter() {
        savedDate = dueDate;
        dueDate = LocalDate.MAX;
    }
}
