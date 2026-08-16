package com.mam.ui.dialog;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.mam.model.MileageTask;
import com.mam.model.Task;
import com.mam.model.TaskKind;
import com.mam.model.TimeTask;
import com.mam.ui.component.CharacterCountTextField;
import com.mam.ui.layout.VerticalFillLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewTaskDialog extends JDialog {
    static final Dimension SIZE = new Dimension(500, 500);
    static String TIME_TASK_STRING = "3. Enter the date by which the task is due.";
    static String MIlEAGE_TASK_STRING = "3. Enter the odometer reading at which the task is due.";

    private final Font labelFont;

    private JComboBox<String> taskKindComboBox;
    private CharacterCountTextField taskKindField;
    private JTextArea noteArea;
    private JComboBox<String> taskModeComboBox;
    private JLabel infoLabel;
    private DatePicker datePicker;
    private CharacterCountTextField odometerField;
    private JButton addButton;

    private int currentSelection = 0;
    private Task task;

    public NewTaskDialog(Frame owner) {
        super(owner, "Add New Task", true);
        labelFont = UIManager.getFont("Label.font").deriveFont(15f);
        initDialog();
        initComponents();
        initTaskKindComboBox();
        initTaskModeComboBox();
        addActionHandlers();
    }

    private void initDialog() {
        setSize(SIZE);
        setMinimumSize(SIZE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel container = new JPanel();
        container.setLayout(new VerticalFillLayout(10, 5));
        container.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel taskTypeLabel = new JLabel("<html>1. Choose Task Kind <span style='color:red'>*</span></html>");
        taskTypeLabel.setFont(taskTypeLabel.getFont().deriveFont(15f));
        container.add(taskTypeLabel);

        // Choose TaskKind
        // e.g., BATTERY_REPLACEMENT, BRAKE_PAD_REPLACEMENT, ...
        taskKindComboBox = new JComboBox<>();
        taskKindComboBox.setFont(labelFont);
        container.add(taskKindComboBox);

        // Field to input custom task name
        taskKindField = new CharacterCountTextField(50, CharacterCountTextField.TEXT, "example: Regular Service", false);
        taskKindField.setFont(labelFont);
        taskKindField.setEnabled(false);
        container.add(taskKindField);

        JLabel noteAreaLabel = new JLabel("<html>2. Special Note <span style='font-style: italic; font-weight: bold;'>(Optional)</span></html>");
        noteAreaLabel.setFont(labelFont);
        container.add(noteAreaLabel);

        // Special note input
        noteArea = new JTextArea();
        noteArea.setFont(labelFont);
        noteArea.setRows(4);
        JScrollPane scrollPane = new JScrollPane(noteArea);
        scrollPane.setMinimumSize(noteArea.getPreferredSize());
        container.add(scrollPane);

        // Choose TimeTask or MileageTask
        taskModeComboBox = new JComboBox<>();
        taskModeComboBox.setFont(labelFont);
        container.add(taskModeComboBox);

        infoLabel = new JLabel(TIME_TASK_STRING);
        infoLabel.setFont(labelFont);
        container.add(infoLabel);

        // datePicker for TimeTask
        DatePickerSettings settings = new DatePickerSettings();
        datePicker = new DatePicker(settings);
        datePicker.setFont(labelFont);
        settings.setAllowKeyboardEditing(false);
        container.add(datePicker);

        // odometerField for MileageTask
        odometerField = new CharacterCountTextField(7, CharacterCountTextField.NUMBER, "example: 1500", false);
        odometerField.setFont(labelFont);
        odometerField.setVisible(false);
        container.add(odometerField);

        addButton = new JButton("Add");
        addButton.setFont(labelFont);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(34, 133, 225));
        container.add(addButton);

        add(container);
    }

    private void initTaskKindComboBox() {
        for (var task : TaskKind.values()) {
            taskKindComboBox.addItem(task.toString());
        }
        taskKindComboBox.addActionListener(_ -> {
            currentSelection = taskKindComboBox.getSelectedIndex();
            taskKindField.setEnabled(currentSelection == taskKindComboBox.getItemCount() - 1);
        });
    }

    private void initTaskModeComboBox() {
        taskModeComboBox.addItem("Time Task");
        taskModeComboBox.addItem("Mileage Task");
        taskModeComboBox.addActionListener(_ -> {
            int sel = taskModeComboBox.getSelectedIndex();
            if (sel == 0) {
                infoLabel.setText(TIME_TASK_STRING);
                datePicker.setVisible(true);
                odometerField.setVisible(false);
            } else {
                infoLabel.setText(MIlEAGE_TASK_STRING);
                datePicker.setVisible(false);
                odometerField.setVisible(true);
            }
        });
    }

    private void addActionHandlers() {
        addButton.addActionListener(_ -> {
            if (handleTaskCreation()) {
                dispose();
            }
        });
    }

    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private boolean handleTaskCreation() {
        int itemCount = taskKindComboBox.getItemCount() - 1;
        int taskMode = taskModeComboBox.getSelectedIndex();
        if (currentSelection == itemCount) {
            if (taskKindField.isEmpty()) {
                showErrorMessage("Please enter Task Name", "Empty");
                return false;
            }
        }
        if (taskMode == 0 && datePicker.getDate() == null) {
            showErrorMessage("Please select a Date", "Date Null");
            return false;
        }
        if (taskMode == 1 && odometerField.isEmpty()) {
            showErrorMessage("Please enter Odometer value", "Empty");
            return false;
        }

        if (taskModeComboBox.getSelectedIndex() == 0) {
            // It's a TimeTask
            if (currentSelection == itemCount) {
                task = TimeTask.create(taskKindField.getText(), noteArea.getText(), datePicker.getDate());
            } else {
                task = TimeTask.create(TaskKind.values()[currentSelection], noteArea.getText(), datePicker.getDate());
            }
        } else {
            // It's a MileageTask
            int reading = Integer.parseInt(odometerField.getText());
            if (currentSelection == itemCount) {
                task = MileageTask.create(taskKindField.getText(), noteArea.getText(), reading);
            } else {
                task = MileageTask.create(TaskKind.values()[currentSelection], noteArea.getText(), reading);
            }
        }
        return true;
    }

    public Task GetTask() {
        return task;
    }
}
