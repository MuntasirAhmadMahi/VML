package com.mam.controller;

import com.mam.model.Vehicle;
import com.mam.repository.VehicleRepository;
import com.mam.ui.component.ScalableImageIcon;
import com.mam.ui.panel.MainPanel;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MainController {
    private final MainPanel panel;
    private final VehicleRepository repository;
    private int vehicleCount;

    public MainController(MainPanel panel, VehicleRepository repository) {
        this.panel = panel;
        this.repository = repository;
    }

    // This should usually be called on start-up
    public void loadVehicles() {
        panel.listClear();
        List<Vehicle> vehicles = repository.getAllVehicles();
        vehicles.sort(Comparator.comparing(Vehicle::getId));
        vehicleCount = vehicles.size();
        panel.setVehicleCount(vehicleCount);
        panel.setVehicles(vehicles);
        panel.listReload();
    }

    public void createAndAddVehicle(Vehicle vehicle) {
        repository.saveVehicle(vehicle);
        panel.addVehicle(vehicle);
        vehicleCount++;
        panel.setVehicleCount(vehicleCount);
        panel.listReload();
    }

    public void changeVehicleImage(UUID vehicleId, String image) {
        if (image == null)
            return;
        // First save the image
        Vehicle vehicle = repository.readVehicle(vehicleId);
        vehicle.setImage(image);
        repository.saveVehicle(vehicle);
        // The load it
        try {
            ScalableImageIcon imageIcon = new ScalableImageIcon(100, 100);
            imageIcon.setImage(new File(image));
            panel.changeVehicleImage(vehicleId, imageIcon);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeRegistrationExpiryDate(UUID vehicleId, LocalDate date) {
        if (date == null)
            return;
        Vehicle vehicle = repository.readVehicle(vehicleId);
        vehicle.renewRegistration(date);
        repository.saveVehicle(vehicle);
        panel.changeVehicleRegistrationExpiryDate(vehicleId, date);
    }

    public void removeVehicle(UUID vehicleId) {
        repository.deleteVehicle(vehicleId);
        panel.removeVehicle(vehicleId);
        vehicleCount--;
        panel.setVehicleCount(vehicleCount);
    }

    public LocalDate getRegistrationExpiryDate(UUID vehicleId) {
        Vehicle vehicle = repository.readVehicle(vehicleId);
        if (vehicle == null) {
            return null;
        }
        return vehicle.getRegistrationExpiryDate();
    }
}
