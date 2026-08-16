package com.mam.ui.dialog;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.mam.ui.layout.VerticalFillLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class RegRenewDialog extends JDialog {
    static final Dimension SIZE = new Dimension(270, 150);
    private final LocalDate currentRegistrationExpiryDate;
    private DatePicker datePicker;
    private JButton okButton;
    private JButton cancelButton;
    private LocalDate selectedDate;


    public RegRenewDialog(Frame owner, LocalDate currentRegistrationExpiryDate) {
        super(owner, "New Date", true);
        this.currentRegistrationExpiryDate = currentRegistrationExpiryDate;
        initDialog();
        initComponents();
        addActionHandlers();
    }

    private void initDialog() {
        setSize(SIZE);
        setMinimumSize(SIZE);
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel container = new JPanel();
        container.setBorder(new EmptyBorder(5, 5, 5, 5));
        container.setLayout(new VerticalFillLayout(10, 5));

        DatePickerSettings datePickerSettings = new DatePickerSettings();
        datePicker = new DatePicker(datePickerSettings);
        if (currentRegistrationExpiryDate != null) {
            datePickerSettings.setDateRangeLimits(currentRegistrationExpiryDate.plusDays(1), LocalDate.MAX);
        } else {
            datePickerSettings.setDateRangeLimits(LocalDate.now(), LocalDate.MAX);
        }
        datePickerSettings.setAllowKeyboardEditing(false);

        okButton = new JButton("OK");
        okButton.setForeground(Color.WHITE);
        okButton.setBackground(new Color(34, 133, 225));

        cancelButton = new JButton("Cancel");

        container.add(datePicker);
        container.add(okButton);
        container.add(cancelButton);
        add(container);
    }

    private void addActionHandlers() {
        okButton.addActionListener(e -> {
            selectedDate = datePicker.getDate();
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
}
