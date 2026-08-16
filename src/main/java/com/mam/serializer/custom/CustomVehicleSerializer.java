package com.mam.serializer.custom;

import com.mam.model.Vehicle;
import com.mam.serializer.VehicleSerializer;

import java.time.LocalDate;
import java.util.UUID;

public class CustomVehicleSerializer implements VehicleSerializer<String> {
    @Override
    public String serialize(Vehicle vehicle) {
        CustomFormatWriter writer = new CustomFormatWriter();

        writer.write(vehicle.getId().toString());
        writer.write(vehicle.getBrand());
        writer.write(vehicle.getModel());
        writer.write(vehicle.getOdometerReading());
        if (vehicle.getRegistrationExpiryDate() == null) {
            writer.write(null);
        } else {
            writer.write(vehicle.getRegistrationExpiryDate().toString());
        }
        writer.write(vehicle.getImage());

        return writer.result();
    }

    @Override
    public Vehicle deserialize(String data) {
        CustomFormatReader reader = new CustomFormatReader(data);
        Vehicle vehicle = new Vehicle(
                UUID.fromString(reader.readString(reader.readInt())),
                reader.readString(reader.readInt()),
                reader.readString(reader.readInt()),
                reader.readInt(),
                null,
                null
//                LocalDate.parse(reader.readString(reader.readInt())), it can be null
//                reader.readString(reader.readInt())
        );
        int l = reader.readInt();
        if (l > 0) {
            vehicle.setRegistrationExpiryDate(LocalDate.parse(reader.readString(l)));
        }
        vehicle.setImage(reader.readString(reader.readInt()));

        return vehicle;
    }
}
