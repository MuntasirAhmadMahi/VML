package com.mam.repository;

import com.mam.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository {

    void saveTask(UUID vehicleId, Task task);

    Task readTask(UUID vehicleId, UUID taskId);

    void deleteTask(UUID vehicleId, UUID taskId);

    List<Task> getAllTasks(UUID vehicleId);
}
