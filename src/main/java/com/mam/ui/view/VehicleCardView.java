package com.mam.ui.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.mam.ui.component.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Consumer;

public class VehicleCardView extends RoundedPanel {
    private UUID id;
    // Row 1
    private JLabel nameLabel;
    private FlatButton optionButton;
    // Row 2
    private JLabel odometerValueLabel;
    // Row 3
    private JLabel expiryDateValueLabel;
    // Row 4
    private JLabel remainingDaysLabel;

    // Popup Menu
    private JPopupMenu popupMenu;

    // Callbacks
    private Consumer<UUID> onItemCallback;
    private Consumer<UUID> onChangeImageCallback;
    private Consumer<UUID> onRenewRegistrationCallback;
    private Consumer<UUID> onDeleteItemCallback;

    public VehicleCardView() {
        setLayout(new GridBagLayout());
        initComponents();
        initPopUpMenu();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onItemCallback != null) {
                    onItemCallback.accept(id);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setOdometerReading(int odometerReading) {
        odometerValueLabel.setText(odometerReading + " KM");
    }

    public void setRegistrationExpiryDate(LocalDate date) {
        if (date == null) {
            expiryDateValueLabel.setText("(Not Set)");
        } else {
            expiryDateValueLabel.setText(date.toString());
        }
    }

    public void setImage(ImageIcon image) {
        if (image != null) {
            nameLabel.setIcon(image);
        }
    }


    public void setRemainingDays(int remainingDays) {
        if (remainingDays == Integer.MIN_VALUE) {
            remainingDaysLabel.setText("");
        } else if (remainingDays == -1) {
            remainingDaysLabel.setForeground(Color.RED);
            remainingDaysLabel.setText("(Ended Yesterday)");
        } else if (remainingDays < -1) {
            remainingDaysLabel.setForeground(Color.RED);
            remainingDaysLabel.setText("(Ended " + (-remainingDays) + " Days Ago)");
        } else if (remainingDays == 1) {
            remainingDaysLabel.setForeground(new Color(245, 103, 2));
            remainingDaysLabel.setText("(Ends Tomorrow)");
        } else if (remainingDays > 1) {
            remainingDaysLabel.setForeground(Color.GREEN);
            remainingDaysLabel.setText("(" + remainingDays + " Days Remaining)");
        } else {
            remainingDaysLabel.setForeground(new Color(245, 103, 2));
            remainingDaysLabel.setText("(Ends Today)");
        }
    }

    public void setOnItem(Consumer<UUID> callback) {
        onItemCallback = callback;
    }

    public void setOnChangeImage(Consumer<UUID> callback) {
        onChangeImageCallback = callback;
    }

    public void setOnRenewRegistration(Consumer<UUID> callback) {
        onRenewRegistrationCallback = callback;
    }

    public void setOnDelete(Consumer<UUID> callback) {
        onDeleteItemCallback = callback;
    }

    private void initComponents() {
        // Row 1
        nameLabel = new JLabel();
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 20));
        optionButton = new FlatButton();
        optionButton.setButtonType(FlatButton.ButtonType.toolBarButton);
        optionButton.setIcon(new FlatSVGIcon("more-vert.svg", 20, 20));

        // Row 2
        JLabel odometerReadingLabel = new JLabel("Odometer Reading");
        odometerReadingLabel.setFont(odometerReadingLabel.getFont().deriveFont(16f));
        odometerReadingLabel.setIcon(new FlatSVGIcon("speedometer.svg", 19, 19));
        odometerValueLabel = new JLabel();
        odometerValueLabel.setFont(odometerValueLabel.getFont().deriveFont(Font.BOLD, 16));

        // Row 3
        JLabel expiryDateLabel = new JLabel("Registration Expiry Date");
        expiryDateLabel.setFont(expiryDateLabel.getFont().deriveFont(16f));
        expiryDateLabel.setIcon(new FlatSVGIcon("clock.svg", 19, 19));
        expiryDateValueLabel = new JLabel();
        expiryDateValueLabel.setFont(expiryDateValueLabel.getFont().deriveFont(Font.BOLD, 16));

        // Row 4
        remainingDaysLabel = new JLabel();
        remainingDaysLabel.setFont(remainingDaysLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC, 16f));

        addCell(0, 0, nameLabel, GridBagConstraints.LINE_START);
        addCell(0, 1, optionButton, GridBagConstraints.NORTHEAST);

        addCell(1, 0, odometerReadingLabel, GridBagConstraints.LINE_START);
        addCell(1, 1, odometerValueLabel, GridBagConstraints.LINE_END);

        addCell(2, 0, expiryDateLabel, GridBagConstraints.LINE_START);
        addCell(2, 1, expiryDateValueLabel, GridBagConstraints.LINE_END);

        addCell(3, 1, remainingDaysLabel, GridBagConstraints.LINE_END);
    }

    private void addCell(int row, int column, Component component, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.anchor = anchor;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        add(component, gbc);
    }

    private void initPopUpMenu() {
        popupMenu = new JPopupMenu();

        JMenuItem changeImageItem = new JMenuItem("Change Image");
        JMenuItem renewRegistrationItem = new JMenuItem("Renew Registration");
        JMenuItem deleteItem = new JMenuItem("Delete");

        popupMenu.add(changeImageItem);
        popupMenu.add(renewRegistrationItem);
        popupMenu.add(deleteItem);

        changeImageItem.addActionListener(_ -> {
            if (onChangeImageCallback != null) {
                onChangeImageCallback.accept(id);
            }
        });
        renewRegistrationItem.addActionListener(_ -> {
            if (onRenewRegistrationCallback != null) {
                onRenewRegistrationCallback.accept(id);
            }

        });
        deleteItem.addActionListener(_ -> {
            if (onDeleteItemCallback != null) {
                onDeleteItemCallback.accept(id);
            }
        });

        optionButton.addActionListener(_ -> {
            int x = optionButton.getWidth() - popupMenu.getPreferredSize().width;
            int y = optionButton.getHeight();
            popupMenu.show(optionButton, x, y);
        });
    }
}
