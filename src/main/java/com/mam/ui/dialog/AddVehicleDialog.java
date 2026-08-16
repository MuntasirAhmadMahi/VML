package com.mam.ui.dialog;

import com.formdev.flatlaf.util.SystemFileChooser;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.mam.model.Vehicle;
import com.mam.ui.component.CharacterCountTextField;
import com.mam.ui.component.ImageView;
import com.mam.ui.layout.VerticalFillLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class AddVehicleDialog extends JDialog {
    static final Dimension SIZE = new Dimension(500, 600);
    private final Font labelFont;
    private JPanel containerPanel;
    private JLabel brandLabel;
    private CharacterCountTextField brandField;
    private JLabel modelLabel;
    private CharacterCountTextField modelField;
    private JLabel odometerReadingLabel;
    private CharacterCountTextField odometerReadingField;
    private JLabel expiryDateLabel;
    private DatePicker datePicker;
    private JLabel imageLabel;
    private ImageView imageView;
    private JButton changeImageButton;
    private JButton addVehicleButton;
    private Vehicle vehicle = null;
    private String image;

    public AddVehicleDialog(Frame owner) {
        super(owner, "Add New Vehicle", true);
        labelFont = UIManager.getFont("Label.font").deriveFont(15f);
        initDialog();
        initComponents();
        addComponents();
        addListeners();
    }

    private void initDialog() {
        setSize(SIZE);
        setMinimumSize(SIZE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        containerPanel = new JPanel();
        containerPanel.setLayout(new VerticalFillLayout(10, 5));
        containerPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        brandLabel = new JLabel("<html>1. Brand <span style='color:red'>*</span></html>");
        brandLabel.setFont(labelFont);
        brandField = new CharacterCountTextField(30, CharacterCountTextField.TEXT, "example: Toyota", false);
        brandField.setFont(labelFont);

        modelLabel = new JLabel("<html>2. Model <span style='color:red'>*</span></html>");
        modelLabel.setFont(labelFont);
        modelField = new CharacterCountTextField(30, CharacterCountTextField.TEXT, "example: Corolla", false);
        modelField.setFont(labelFont);

        odometerReadingLabel = new JLabel("<html>3. Initial Odometer Reading <span style='color:red'>*</span></html>");
        odometerReadingLabel.setFont(labelFont);
        odometerReadingField = new CharacterCountTextField(7, CharacterCountTextField.NUMBER, "example: 1500", false);
        odometerReadingField.setFont(labelFont);

        expiryDateLabel = new JLabel("4. Registration Expiry Date");
        expiryDateLabel.setFont(labelFont);
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        datePicker = new DatePicker(datePickerSettings);
        datePicker.setFont(labelFont);
        datePicker.setDateToToday();
        datePickerSettings.setDateRangeLimits(LocalDate.now(), LocalDate.MAX);
        datePickerSettings.setAllowKeyboardEditing(false);

        imageLabel = new JLabel("5. Image");
        imageLabel.setFont(labelFont);
        imageView = new ImageView(150, 150);
        changeImageButton = new JButton("Choose Image");
        changeImageButton.setFont(labelFont);

        addVehicleButton = new JButton("Add Vehicle");
        addVehicleButton.setFont(labelFont);
        addVehicleButton.setForeground(Color.WHITE);
        addVehicleButton.setBackground(new Color(34, 133, 225));
    }

    private void addComponents() {
        containerPanel.add(brandLabel);
        containerPanel.add(brandField);

        containerPanel.add(modelLabel);
        containerPanel.add(modelField);

        containerPanel.add(odometerReadingLabel);
        containerPanel.add(odometerReadingField);

        containerPanel.add(expiryDateLabel);
        containerPanel.add(datePicker);

        containerPanel.add(imageLabel);
        containerPanel.add(imageView);
        containerPanel.add(changeImageButton);

        containerPanel.add(addVehicleButton);
        add(containerPanel);
    }

    private void addListeners() {
        changeImageButton.addActionListener(_ -> handleFileChooser());
        addVehicleButton.addActionListener(_ -> handleAddVehicle());
    }

    private void handleFileChooser() {
        SystemFileChooser fileChooser = new SystemFileChooser();
        fileChooser.addChoosableFileFilter(
                new SystemFileChooser.FileNameExtensionFilter("Image", "jpg", "png", "jpeg")
        );

        if (fileChooser.showOpenDialog(this) == SystemFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            image = file.getAbsolutePath();
            try {
                imageView.setImage(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleAddVehicle() {
        if (brandField.isEmpty() || modelField.isEmpty() || odometerReadingField.isEmpty()) {
            return;
        }
        String brand = brandField.getText();
        String model = modelField.getText();
        int odometerReading = Integer.parseInt(odometerReadingField.getText());
        LocalDate registrationExpiryDate = datePicker.getDate();

        vehicle = Vehicle.create(
                brand,
                model,
                odometerReading,
                registrationExpiryDate,
                image
        );
        dispose();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
