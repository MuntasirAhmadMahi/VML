package com.mam.model;

import java.time.LocalDate;
import java.util.UUID;

public class Vehicle {
    private final UUID id;
    private final String brand;
    private final String model;
    private int odometerReading;
    private LocalDate registrationExpiryDate;
    private String image;

    public Vehicle(UUID id, String brand, String model, int odometerReading,
                   LocalDate registrationExpiryDate, String image) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.odometerReading = odometerReading;
        this.registrationExpiryDate = registrationExpiryDate;
        this.image = image;
    }

    public static Vehicle create(String brand, String model, int odometerReading,
                                 LocalDate registrationExpiryDate, String image) {
        return new Vehicle(UUID.ofEpochMillis(System.currentTimeMillis()), brand, model, odometerReading, registrationExpiryDate, image);
    }

    public void setRegistrationExpiryDate(LocalDate date) {
        this.registrationExpiryDate = date;
    }

    public void setOdometerReading(int reading) {
        this.odometerReading = reading;
    }
    // Getters
    public UUID getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getOdometerReading() { return odometerReading; }
    public LocalDate getRegistrationExpiryDate() { return registrationExpiryDate; }
    public String getImage() { return image; }

    public String getVehicleName() { return brand + " " + model; }

    // Setters
    public void updateOdometerReading(int newReading) {
        if (newReading < odometerReading) {
            throw new IllegalArgumentException("Odometer reading cannot decrease.");
        }
        odometerReading = newReading;
    }

    public void renewRegistration(LocalDate newDate) {
        if (registrationExpiryDate != null && newDate.isBefore(registrationExpiryDate)) {
            throw new IllegalArgumentException("Registration date cannot be earlier than the current expiry date.");
        }
        registrationExpiryDate = newDate;
    }

    public void setImage(String image) { this.image = image; }
}
