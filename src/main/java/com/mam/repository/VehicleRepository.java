package com.mam.repository;

import com.mam.model.Vehicle;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository {
    void saveVehicle(Vehicle vehicle);

    Vehicle readVehicle(UUID vehicleId);

    void deleteVehicle(UUID vehicleId);

    List<Vehicle> getAllVehicles();
}