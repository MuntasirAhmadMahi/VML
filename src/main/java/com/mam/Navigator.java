package com.mam;

import java.util.UUID;

public interface Navigator {
    void showMainPanel();

    void showTaskPanel(UUID vehicleId);
}
