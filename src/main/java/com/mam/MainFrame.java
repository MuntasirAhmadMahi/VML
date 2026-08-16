package com.mam;

import com.mam.controller.MainController;
import com.mam.controller.TaskController;
import com.mam.repository.TaskRepository;
import com.mam.repository.VehicleRepository;
import com.mam.repository.custom.CustomTaskRepository;
import com.mam.repository.custom.CustomVehicleRepository;
import com.mam.serializer.TaskSerializer;
import com.mam.serializer.VehicleSerializer;
import com.mam.serializer.custom.CustomTaskSerializer;
import com.mam.serializer.custom.CustomVehicleSerializer;
import com.mam.ui.panel.MainPanel;
import com.mam.ui.panel.TaskPanel;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class MainFrame extends JFrame implements Navigator {
    static final Dimension SIZE = new Dimension(500, 650);
    private Container container;
    private CardLayout cardLayout;

    // Repositories
    private VehicleRepository vehicleRepository;
    private TaskRepository taskRepository;

    // Panels
    private MainPanel mainPanel;
    private TaskPanel taskPanel;

    // Controllers
    private MainController mainController;
    private TaskController taskController;

    public MainFrame() {
        initFrame();
        initRepositories();
        initOtherPanels();
        initControllers();
        mainController.loadVehicles();
    }

    private void initFrame() {
        setTitle("Vehicle Maintenance Log");
        setSize(SIZE);
        setMinimumSize(SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        container = getContentPane();

        cardLayout = new CardLayout();
        container.setLayout(cardLayout);
    }

    private void initRepositories() {
        VehicleSerializer vehicleSerializer = new CustomVehicleSerializer();
        vehicleRepository = new CustomVehicleRepository(vehicleSerializer);
        TaskSerializer taskSerializer = new CustomTaskSerializer();
        taskRepository = new CustomTaskRepository(taskSerializer);
    }

    private void initOtherPanels() {
        mainPanel = new MainPanel(this);
        taskPanel = new TaskPanel(this);

        container.add(mainPanel, "MAIN");
        container.add(taskPanel, "TASK");
    }

    private void initControllers() {
        mainController = new MainController(mainPanel, vehicleRepository);
        taskController = new TaskController(taskPanel, vehicleRepository, taskRepository);

        // Now we need to set the controller on each panel
        mainPanel.setController(mainController);
        taskPanel.setController(taskController);
    }

    @Override
    public void showMainPanel() {
        cardLayout.show(container, "MAIN");
    }

    @Override
    public void showTaskPanel(UUID vehicleId) {
        taskController.initialize(vehicleId);
        cardLayout.show(container, "TASK");
    }
}