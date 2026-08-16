package com.mam.controller;

import com.mam.model.DueInfo;
import com.mam.model.DueStatus;
import com.mam.model.Task;
import com.mam.model.Vehicle;
import com.mam.repository.TaskRepository;
import com.mam.repository.VehicleRepository;
import com.mam.ui.panel.TaskPanel;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskController {
    private final TaskPanel panel;
    private final VehicleRepository vehicleRepository;
    private final TaskRepository taskRepository;
    private Vehicle currentVehicle;

    public TaskController(TaskPanel panel, VehicleRepository vehicleRepository, TaskRepository taskRepository) {
        this.panel = panel;
        this.vehicleRepository = vehicleRepository;
        this.taskRepository = taskRepository;
    }

    public void initialize(UUID vehicleId) {
        panel.setVehicleId(vehicleId);
        currentVehicle = vehicleRepository.readVehicle(vehicleId);
        panel.setOdometerReading(currentVehicle.getOdometerReading());
        loadTasks(0);
        updateComboBoxValues();
    }

    // TODO
    //  - cache tasks to improve performance
    public void loadTasks(int type) {
        panel.listClear();
        List<Task> allTasks = taskRepository.getAllTasks(panel.getVehicleId());
        if (type == 0) {
            panel.setTasks(allTasks, currentVehicle.getOdometerReading());
            return;
        }
        switch (type) {
            case 1 ->
                    panel.setTasks(loadTaskFiltered(allTasks, DueStatus.DUE_NOW), currentVehicle.getOdometerReading());
            case 2 ->
                    panel.setTasks(loadTaskFiltered(allTasks, DueStatus.DUE_SOON), currentVehicle.getOdometerReading());
            case 3 ->
                    panel.setTasks(loadTaskFiltered(allTasks, DueStatus.OVERDUE), currentVehicle.getOdometerReading());
            case 4 ->
                    panel.setTasks(loadTaskFiltered(allTasks, DueStatus.UPCOMING), currentVehicle.getOdometerReading());
            case 5 ->
                    panel.setTasks(loadTaskFiltered(allTasks, DueStatus.COMPLETED), currentVehicle.getOdometerReading());
            default -> throw new RuntimeException("Invalid task type '" + type + "'. Expected one of 0, 1, 2, 3, 4.");
        }
    }

    private List<Task> loadTaskFiltered(List<Task> allTasks, DueStatus status) {
        List<Task> tasks = new ArrayList<>();
        for (var task : allTasks) {
            if (task.getDueInfo(LocalDate.now(), currentVehicle.getOdometerReading()).status().equals(status)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    // TODO: performance improvement
    public void updateComboBoxValues() {
        List<Task> tasks = taskRepository.getAllTasks(currentVehicle.getId());
        int all = tasks.size();
        int dueNow = 0, dueSoon = 0, overdue = 0, upcoming = 0, completed = 0;
        int totalDue = 0;
        for (var task : tasks) {
            if (task.isDue()) {
                totalDue++;
            }
            switch (task.getDueInfo(LocalDate.now(), currentVehicle.getOdometerReading()).status()) {
                case DUE_NOW -> dueNow++;
                case DUE_SOON -> dueSoon++;
                case OVERDUE -> overdue++;
                case UPCOMING -> upcoming++;
                case COMPLETED -> completed++;
            }
        }
        panel.setDueCount(totalDue);
        panel.setCountAt(0, all);
        panel.setCountAt(1, dueNow);
        panel.setCountAt(2, dueSoon);
        panel.setCountAt(3, overdue);
        panel.setCountAt(4, upcoming);
        panel.setCountAt(5, completed);
    }

    public void addTask(Task task) {
        taskRepository.saveTask(panel.getVehicleId(), task);
        updateComboBoxValues();

        DueInfo dueInfo = task.getDueInfo(LocalDate.now(), currentVehicle.getOdometerReading());
        int sel = panel.getCurrentSelection();
        if (sel == 0 || dueInfo.status().ordinal() + 1 == sel) {
            panel.addTask(task, currentVehicle.getOdometerReading());
            panel.listReload();
        }
    }

    public void removeTask(UUID taskId) {
        taskRepository.deleteTask(panel.getVehicleId(), taskId);
        updateComboBoxValues();
        panel.removeTask(taskId);
        panel.listReload();
    }

    public void updateOdometerReading(int reading) {
        try {
            currentVehicle.updateOdometerReading(reading);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    panel,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        vehicleRepository.saveVehicle(currentVehicle);
        panel.setOdometerReading(currentVehicle.getOdometerReading());
        loadTasks(panel.getCurrentSelection());
        updateComboBoxValues();
        panel.listReload();
    }

    // Save vehicle reading
    // We need to change odometer reading
    // Combo box values
    // Realod tasks
    public void resetOdometerReading() {
        currentVehicle.setOdometerReading(0);
        vehicleRepository.saveVehicle(currentVehicle);
        panel.setOdometerReading(0);
        loadTasks(panel.getCurrentSelection());
        updateComboBoxValues();
        panel.listReload();
    }

    public void resetTaskCounter(UUID taskId) {
        Task task = taskRepository.readTask(currentVehicle.getId(), taskId);
        task.resetCounter();
        taskRepository.saveTask(currentVehicle.getId(), task);
        loadTasks(panel.getCurrentSelection());
        updateComboBoxValues();
        panel.listReload();
    }
}
