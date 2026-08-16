package com.mam.serializer;

import com.mam.model.Vehicle;

public interface VehicleSerializer<T> {
    T serialize(Vehicle vehicle);
    Vehicle deserialize(T data);
}
