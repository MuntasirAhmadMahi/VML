package com.mam.ui.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.mam.Navigator;
import com.mam.controller.TaskController;
import com.mam.model.*;
import com.mam.ui.component.CircularStat;
import com.mam.ui.dialog.NewTaskDialog;
import com.mam.ui.dialog.OdometerEditDialog;
import com.mam.ui.layout.VerticalFillLayout;
import com.mam.ui.view.TaskCardView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskPanel extends JPanel {
    static final Map<TaskKind, String> icons = Map.ofEntries(
            Map.entry(TaskKind.BATTERY_REPLACEMENT, "battery.svg"),
            Map.entry(TaskKind.BRAKE_PAD_REPLACEMENT, "brake.svg"),
            Map.entry(TaskKind.ENGINE_INSPECTION, "engine-inspection.svg"),
            Map.entry(TaskKind.ENGINE_OIL_CHANGE, "engine-oil.svg"),
            Map.entry(TaskKind.EXHAUST_SYSTEM_INSPECTION, "exhaust-pipe.svg"),
            Map.entry(TaskKind.SPARK_PLUG_REPLACEMENT, "spark-plug.svg"),
            Map.entry(TaskKind.SUSPENSION_INSPECTION, "suspension.svg"),
            Map.entry(TaskKind.TRANSMISSION_OIL_CHANGE, "transmission-oil.svg"),
            Map.entry(TaskKind.TIRE_REPLACEMENT, "tire.svg"),
            Map.entry(TaskKind.WHEEL_ALIGNMENT_AND_BALANCING, "wheel-alignment.svg"),
            Map.entry(TaskKind.CUSTOM, "maintenance.svg")
    );
    private static final String[] options =
            {"All Tasks", "Due Now", "Due Soon", "Overdue", "Upcoming", "Completed"};
    private static final String[] optionColors =
            {"#000000", "#F56702", "#F56702", "#FF0000", "#9370db", "#22A447"};
    private final Navigator navigator;
    private final JLabel dueCountLabel;
    private final JComboBox<String> comboBox;
    private TaskController controller;
    //
    private boolean isComboValueChanging;
    private UUID vehicleId;
    // Top bar buttons
    private FlatButton backButton;
    private FlatButton resetButton;
    // end
    private FlatButton addButton;
    private CircularStat odometerView;
    private FlatButton editButton;
    private JPanel taskListPanel;

    public TaskPanel(Navigator navigator) {
        this.navigator = navigator;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initTopbarPanel();
        initOdometerView();

        // due count
        dueCountLabel = new JLabel();
        dueCountLabel.setFont(dueCountLabel.getFont().deriveFont(18f));
        dueCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dueCountLabel.setHorizontalAlignment(JLabel.CENTER);
        add(Box.createVerticalStrut(5));
        add(dueCountLabel);
        add(Box.createVerticalStrut(5));
        setDueCount(0);

        // comboxBox -> lets you choose task Kind
        comboBox = new JComboBox<>(options);
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getPreferredSize().height));
        add(comboBox);

        initTaskListPanel();
        addActionHandlers();
    }

    /**
     * Initializes a <code>JPanel</code> containing only <code>JButton</code>s.
     * <li><code>backButton</code> is used for navigation.</li>
     * <li><code>historyButton</code> is used for viewing maintenance history.</li>
     * <li><code>reloadButton</code> is used for reloading tasks.</li>
     * <li><code>addButton</code> is used for adding new task.</li>
     */
    private void initTopbarPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setBorder(new EmptyBorder(10, 10, 5, 10));

        backButton = new FlatButton();
        backButton.setIcon(new FlatSVGIcon("arrow-back.svg", 30, 30));
        backButton.setButtonType(FlatButton.ButtonType.toolBarButton);
        backButton.setToolTipText("Back");

        resetButton = new FlatButton();
        resetButton.setIcon(new FlatSVGIcon("reload.svg", 30, 30));
        resetButton.setButtonType(FlatButton.ButtonType.toolBarButton);
        resetButton.setToolTipText("Reset");

        addButton = new FlatButton();
        addButton.setIcon(new FlatSVGIcon("add.svg", 30, 30));
        addButton.setButtonType(FlatButton.ButtonType.toolBarButton);
        addButton.setToolTipText("Add New Task");

        container.add(backButton);
        container.add(Box.createHorizontalGlue());
        container.add(resetButton);
        container.add(addButton);

        add(container);
    }

    private void initOdometerView() {
        JPanel container = new JPanel();
        odometerView = new CircularStat("KM", Color.WHITE, Color.GRAY, 40f, 17f);

        editButton = new FlatButton();
        editButton.setIcon(new FlatSVGIcon("edit.svg"));
        editButton.setButtonType(FlatButton.ButtonType.toolBarButton);
        editButton.setToolTipText("Edit Odometer Reading");

        container.add(odometerView);
        container.add(editButton);

        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, container.getPreferredSize().height));
        add(container);
    }

    private void initTaskListPanel() {
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new VerticalFillLayout(20, 10));

        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);

        add(scrollPane);
    }

    private void addActionHandlers() {
        backButton.addActionListener(_ -> navigator.showMainPanel());
        addButton.addActionListener(_ -> {
            NewTaskDialog dialog = new NewTaskDialog((JFrame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            Task task = dialog.GetTask();
            if (task != null) {
                controller.addTask(task);
            }
        });
        editButton.addActionListener(_ -> {
            OdometerEditDialog dialog = new OdometerEditDialog((JFrame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            int reading = dialog.getEnteredReading();
            if (reading != -1) {
                controller.updateOdometerReading(reading);
            }
        });
        comboBox.addActionListener(_ -> {
            if (!isComboValueChanging) {
                controller.loadTasks(comboBox.getSelectedIndex());
                listReload();
            }
        });
        resetButton.addActionListener(_ -> {
            controller.resetOdometerReading();
        });
    }

    public int getCurrentSelection() {
        return comboBox.getSelectedIndex();
    }

    public void setController(TaskController controller) {
        this.controller = controller;
    }

    public void setCountAt(int index, int count) {
        isComboValueChanging = true;
        int selIndex = comboBox.getSelectedIndex();
        comboBox.removeItemAt(index);

        String itemText = "<html>" + options[index]
                + " <span style='font-weight: bold;color: "
                + optionColors[index] + ";'>(" + count + ")</span></html>";
        if (count < 1)
            itemText = options[index];
        comboBox.insertItemAt(itemText, index);
        comboBox.setSelectedIndex(selIndex);
        isComboValueChanging = false;
    }

    public void setOdometerReading(int odometerReading) {
        odometerView.setCount(odometerReading);
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setDueCount(int count) {
        if (count < 0) return;
        dueCountLabel.setText("<html>Due Count: <span style='font-weight: bold;'>" + count + "</span></html>");
    }

    public void setTasks(List<Task> tasks, int odometerReading) {
        for (var task : tasks) {
            addTask(task, odometerReading);
        }
    }

    public void addTask(Task task, int odometerReading) {
        TaskCardView cardView = new TaskCardView();

        cardView.setId(task.getId());
        cardView.setTitle(task.getName());
        cardView.setNote(task.getNote());
        cardView.setIcon(new FlatSVGIcon(icons.get(task.getType()), 24, 24));
        DueInfo dueInfo = task.getDueInfo(LocalDate.now(), odometerReading);

        // If the task is completed, we won't show tick mark and won't attach any listener to it
        // And show due value to savedDueValue
        boolean taskCompleted = dueInfo.status() == DueStatus.COMPLETED;

        if (task instanceof MileageTask mileageTask) {
            cardView.setDueValue((taskCompleted ? mileageTask.getSavedDueAtOdometer() :mileageTask.getDueAtOdometer()) + " KM");
        } else if (task instanceof TimeTask timeTask) {
            cardView.setDueValue((taskCompleted ? timeTask.getSavedDate().toString() : timeTask.getDueDate().toString()));
        } else {
            throw new RuntimeException("Unsupported task type: " + task.getClass());
        }

        cardView.setDueStatusLabel(dueInfo.description(), optionColors[dueInfo.status().ordinal() + 1]);
        cardView.setOnDelete(this::handleDelete);

        if (taskCompleted) {
            cardView.setMarkAsDoneButtonVisible(false);
        } else {
            cardView.setOnMarkAsDone(this::handleMarkAsDone);
        }

        taskListPanel.add(cardView);
    }

    private void handleMarkAsDone(UUID taskId) {
        controller.resetTaskCounter(taskId);
    }

    private void handleDelete(UUID taskId) {
        controller.removeTask(taskId);
    }

    public void removeTask(UUID taskId) {
        for (int i = 0; i < taskListPanel.getComponentCount(); i++) {
            TaskCardView cardView = (TaskCardView) taskListPanel.getComponent(i);
            if (cardView.getId() == taskId) {
                taskListPanel.remove(i);
                return;
            }
        }
    }

    public void listClear() {
        taskListPanel.removeAll();
    }

    public void listReload() {
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }
}