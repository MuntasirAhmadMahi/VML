package com.mam.ui.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RoundedPanel extends JPanel {
    private final Color accentColor;
    private boolean mouseInside = false;

    public RoundedPanel() {
        accentColor = UIManager.getColor("Component.accentColor");
        setOpaque(false);
        setBorder(new EmptyBorder(7, 7, 7, 7));

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (mouseInside)
                    return;
                mouseInside = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseInside = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // border
        g2.setColor(mouseInside ? accentColor : new Color(0, 0, 0, 30));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

        g2.dispose();
        super.paintComponent(g);
    }
}
