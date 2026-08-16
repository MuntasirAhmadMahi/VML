package com.mam.ui.panel;

import com.formdev.flatlaf.util.SystemFileChooser;
import com.mam.App;
import com.mam.Navigator;
import com.mam.controller.MainController;
import com.mam.model.Vehicle;
import com.mam.ui.component.CircularStat;
import com.mam.ui.component.ScalableImageIcon;
import com.mam.ui.dialog.AddVehicleDialog;
import com.mam.ui.dialog.RegRenewDialog;
import com.mam.ui.layout.VerticalFillLayout;
import com.mam.ui.view.VehicleCardView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class MainPanel extends JPanel {
    private final Navigator navigator;
    private MainController controller;
    private CircularStat countLabel;
    private JButton addButton;
    private JPanel vehicleList;

    public MainPanel(Navigator navigator) {
        this.navigator = navigator;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initHeaderPanel();
        initVehicleListPanel();
        addActionHandlers();
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    private void initHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        countLabel = new CircularStat("Vehicle", Color.WHITE, Color.DARK_GRAY, 30f, 15f);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton = new JButton("Add New Vehicle");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(countLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(addButton);

        add(panel);
    }

    private void initVehicleListPanel() {
        vehicleList = new JPanel();
        vehicleList.setLayout(new VerticalFillLayout(20, 10));

        // List
        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);

        add(scrollPane);
    }

    private void addActionHandlers() {
        addButton.addActionListener(_ -> {
            AddVehicleDialog dialog = new AddVehicleDialog((JFrame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            Vehicle vehicle = dialog.getVehicle();
            if (vehicle != null) {
                controller.createAndAddVehicle(vehicle);
            }
        });
    }

    public void setVehicles(List<Vehicle> vehicles) {
        for (var vehicle : vehicles) {
            addVehicle(vehicle);
        }
    }

    public void addVehicle(Vehicle vehicle) {
        VehicleCardView cardView = new VehicleCardView();

        cardView.setId(vehicle.getId());
        cardView.setName(vehicle.getVehicleName());
        cardView.setOdometerReading(vehicle.getOdometerReading());
        cardView.setRegistrationExpiryDate(vehicle.getRegistrationExpiryDate());
        cardView.setRemainingDays(calculateRemainingDays(vehicle.getRegistrationExpiryDate()));
        if (vehicle.getImage() != null) {
            ScalableImageIcon image = new ScalableImageIcon(100, 100);
            try {
                image.setImage(new File(vehicle.getImage()));
                cardView.setImage(image);
            } catch (Exception e) {
                image.setImage(App.PLACE_HOLDER_IMAGE);
                cardView.setImage(image);
            }
        }

        cardView.setOnItem(navigator::showTaskPanel);
        cardView.setOnChangeImage(this::handleImageSelection);
        cardView.setOnRenewRegistration(this::handleRegistrationRenewal);
        cardView.setOnDelete(id -> controller.removeVehicle(id));

        vehicleList.add(cardView);
    }

    private void handleImageSelection(UUID vehicleId) {
        SystemFileChooser fileChooser = new SystemFileChooser();
        fileChooser.addChoosableFileFilter(
                new SystemFileChooser.FileNameExtensionFilter("Image", "jpg", "png", "jpeg")
        );

        if (fileChooser.showOpenDialog(this) == SystemFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.changeVehicleImage(vehicleId, file.toString());
        }
    }

    private void handleRegistrationRenewal(UUID vehicleId) {
        RegRenewDialog dialog =
                new RegRenewDialog((JFrame) SwingUtilities.getWindowAncestor(this), controller.getRegistrationExpiryDate(vehicleId));
        dialog.setVisible(true);
        controller.changeRegistrationExpiryDate(vehicleId, dialog.getSelectedDate());
    }

    public void changeVehicleImage(UUID vehicleId, ImageIcon image) {
        if (image == null)
            return;
        for (int i = 0; i < vehicleList.getComponentCount(); i++) {
            VehicleCardView cardView = (VehicleCardView) vehicleList.getComponent(i);
            if (cardView.getId().equals(vehicleId)) {
                cardView.setImage(image);
                cardView.revalidate();
                return;
            }
        }
    }

    public void changeVehicleRegistrationExpiryDate(UUID vehicleId, LocalDate date) {
        for (int i = 0; i < vehicleList.getComponentCount(); i++) {
            VehicleCardView cardView = (VehicleCardView) vehicleList.getComponent(i);
            if (cardView.getId().equals(vehicleId)) {
                cardView.setRegistrationExpiryDate(date);
                cardView.setRemainingDays(calculateRemainingDays(date));
                cardView.revalidate();
                return;
            }
        }
    }

    public void removeVehicle(UUID vehicleId) {
        for (int i = 0; i < vehicleList.getComponentCount(); i++) {
            VehicleCardView cardView = (VehicleCardView) vehicleList.getComponent(i);
            if (cardView.getId().equals(vehicleId)) {
                vehicleList.remove(i);
                listReload();
                return;
            }
        }
    }

    public void setVehicleCount(int count) {
        if (count < 0) return;
        countLabel.setCount(count);
        countLabel.setText("Vehicle" + (count > 1 ? "s" : ""));
    }

    public void listReload() {
        vehicleList.revalidate();
        vehicleList.repaint();
    }

    public void listClear() {
        vehicleList.removeAll();
    }

    private int calculateRemainingDays(LocalDate date) {
        if (date == null) {
            return Integer.MIN_VALUE;
        }
        return Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), date));
    }
}
