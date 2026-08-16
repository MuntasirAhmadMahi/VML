package com.mam.ui.dialog;

import com.mam.ui.component.CharacterCountTextField;
import com.mam.ui.layout.VerticalFillLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OdometerEditDialog extends JDialog {
    static final Dimension SIZE = new Dimension(270, 170);

    private CharacterCountTextField odometerField;
    private JButton okButton;
    private JButton cancelButton;
    private int odometerReading = -1;

    public OdometerEditDialog(Frame owner) {
        super(owner, "Change Odometer Reading", true);
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
        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        containerPanel.setLayout(new VerticalFillLayout(10, 5));

        odometerField = new CharacterCountTextField(7, CharacterCountTextField.NUMBER, "example: 1500", false);

        okButton = new JButton("OK");
        okButton.setForeground(Color.WHITE);
        okButton.setBackground(new Color(34, 133, 225));

        cancelButton = new JButton("Cancel");

        containerPanel.add(odometerField);
        containerPanel.add(okButton);
        containerPanel.add(cancelButton);

        add(containerPanel);
    }

    private void addActionHandlers() {
        okButton.addActionListener(_ -> {
            if (odometerField.isEmpty()) {
                return;
            }
            odometerReading = Integer.parseInt(odometerField.getText());
            dispose();
        });
        cancelButton.addActionListener(_ -> dispose());
    }

    public int getEnteredReading() {
        return odometerReading;
    }
}
