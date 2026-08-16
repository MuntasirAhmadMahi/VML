package com.mam.ui.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.mam.ui.component.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

public class TaskCardView extends RoundedPanel {
    private UUID id;

    private JLabel titleLabel;
    private JTextArea noteArea;

    private FlatButton markAsDoneButton;
    private FlatButton deleteButton;

    // Due related
    private JLabel dueValueLabel;
    private JLabel dueStatusLabel;

    // Callbacks
    private Consumer<UUID> markAsDoneCallback;
    private Consumer<UUID> deleteButtonCallback;

    public TaskCardView() {
        initComponents();
        addCallbackHandlers();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        titleLabel = new JLabel();
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        noteArea = new JTextArea();
        noteArea.setEditable(false);
        noteArea.setFocusable(false);
        noteArea.setOpaque(false);
        noteArea.setBorder(null);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        noteArea.setHighlighter(null);

        // Action buttons
        JPanel buttonContainer = new JPanel(); // Contains tick and delete icon
        buttonContainer.setBackground(Color.WHITE);

        markAsDoneButton = new FlatButton();
        markAsDoneButton.setButtonType(FlatButton.ButtonType.toolBarButton);
        markAsDoneButton.setToolTipText("Mark task as done");
        markAsDoneButton.setIcon(new FlatSVGIcon("check-mark.svg", 18, 18));

        deleteButton = new FlatButton();
        deleteButton.setButtonType(FlatButton.ButtonType.toolBarButton);
        deleteButton.setIcon(new FlatSVGIcon("delete.svg", 18, 18));
        deleteButton.setToolTipText("Delete task");
        buttonContainer.add(deleteButton);
        buttonContainer.add(markAsDoneButton);

        dueValueLabel = new JLabel();
        dueValueLabel.setFont(dueValueLabel.getFont().deriveFont(Font.BOLD, 15));
        dueStatusLabel = new JLabel();
        dueStatusLabel.setFont(dueStatusLabel.getFont().deriveFont(Font.ITALIC | Font.BOLD, 15));

        // Add
        addCell(0, 0, titleLabel, GridBagConstraints.LINE_START, GridBagConstraints.NONE);
        addCell(0, 1, buttonContainer, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);

        addCell(1, 0, noteArea, GridBagConstraints.LINE_START, GridBagConstraints.BOTH);
        addCell(1, 1, dueValueLabel, GridBagConstraints.LINE_END, GridBagConstraints.NONE);

        addCell(2, 1, dueStatusLabel, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);
    }

    private void addCell(int row, int column, Component component, int anchor, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = 1;
        add(component, gbc);
    }

    private void addCallbackHandlers() {
        markAsDoneButton.addActionListener(_ -> {
            if (markAsDoneCallback != null) {
                markAsDoneCallback.accept(id);
            }
        });
        deleteButton.addActionListener(_ -> {
            if (deleteButtonCallback != null) {
                deleteButtonCallback.accept(id);
            }
        });
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    public void setIcon(ImageIcon icon) {
        titleLabel.setIcon(icon);
    }

    public void setNote(String text) {
        noteArea.setText(text);
    }

    public void setDueValue(String text) {
        dueValueLabel.setText(text);
    }

    public void setDueStatusLabel(String text, String color) {
        dueStatusLabel.setForeground(Color.decode(color));
        dueStatusLabel.setText(text);
    }

    public void setMarkAsDoneButtonVisible(boolean visible) {
        markAsDoneButton.setVisible(visible);
    }

    public void setOnMarkAsDone(Consumer<UUID> callback) {
        markAsDoneCallback = callback;
    }

    public void setOnDelete(Consumer<UUID> callback) {
        deleteButtonCallback = callback;
    }
}
