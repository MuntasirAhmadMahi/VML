package com.mam.repository.custom;

import com.mam.App;
import com.mam.Util;
import com.mam.model.Task;
import com.mam.repository.TaskRepository;
import com.mam.serializer.TaskSerializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomTaskRepository implements TaskRepository {
    private final TaskSerializer<String> serializer;

    public CustomTaskRepository(TaskSerializer<String> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void saveTask(UUID vehicleId, Task task) {
        try {
            Path path = App.getVehicleFolderFor(vehicleId).resolve(task.getId().toString());
            Files.writeString(path, serializer.serialize(task));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task readTask(UUID vehicleId, UUID taskId) {
        try {
            Path path = App.getVehicleFolderFor(vehicleId).resolve(taskId.toString());
            return serializer.deserialize(Files.readString(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTask(UUID vehicleId, UUID taskId) {
        try {
            Path path = App.getVehicleFolderFor(vehicleId).resolve(taskId.toString());
            Util.deleteFile(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Task> getAllTasks(UUID vehicleId) {
        try {
            Path vehicleFolder = App.getVehicleFolderFor(vehicleId);
            List<Task> tasks = new ArrayList<>();

            try (var paths = Files.list(vehicleFolder)) {
                for (Path p : paths.toList()) {
                    if (p.getFileName().toString().equals("info")) {
                        continue;
                    }
                    tasks.add(readTask(vehicleId, UUID.fromString(p.getFileName().toString())));
                }
            }

            return tasks;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
