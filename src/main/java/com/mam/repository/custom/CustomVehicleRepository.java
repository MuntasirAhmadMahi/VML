package com.mam.repository.custom;

import com.mam.App;
import com.mam.Util;
import com.mam.model.Vehicle;
import com.mam.repository.VehicleRepository;
import com.mam.serializer.VehicleSerializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomVehicleRepository implements VehicleRepository {
    private final VehicleSerializer<String> serializer;

    public CustomVehicleRepository(VehicleSerializer<String> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void saveVehicle(Vehicle vehicle) {
        try {
            Path path = App.getVehicleFolderFor(vehicle.getId());
            Files.createDirectories(App.getVehicleFolderFor(vehicle.getId()));
            path = path.resolve("info");

            Files.writeString(path, serializer.serialize(vehicle));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Vehicle readVehicle(UUID vehicleId) {
        try {
            Path path = App.getVehicleFolderFor(vehicleId).resolve("info");
            return serializer.deserialize(Files.readString(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteVehicle(UUID vehicleId) {
        try {
            Path path = App.getVehicleFolderFor(vehicleId);
            Util.deleteFile(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        try {
            Path path = App.getAppDataDirectory();
            List<Vehicle> vehicles = new ArrayList<>();

            try (var paths = Files.list(path)) {
                for (Path p : paths.toList()) {
                    vehicles.add(readVehicle(UUID.fromString(p.getFileName().toString())));
                }
            }

            return vehicles;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
