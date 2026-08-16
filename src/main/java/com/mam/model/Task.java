package com.mam.model;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Task implements Serviceable {
    protected UUID id;
    protected TaskKind type;
    protected String name;
    protected String note;

    public abstract boolean isDue();

    public abstract DueInfo getDueInfo(LocalDate today, int currentOdometer);

    public UUID getId() {
        return id;
    }

    public TaskKind getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }
}
