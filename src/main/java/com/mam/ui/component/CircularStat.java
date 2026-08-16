package com.mam.ui.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CircularStat extends JPanel {
    private final JLabel countLabel;
    private final JLabel textLabel;
    private final Color backgroundColor;
    private final Color borderColor;

    public CircularStat(String text, Color backgroundColor, Color borderColor, float countFontSize, float textFontSize) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        // initialize components
        countLabel = new JLabel("0"); // use 0 as initial value
        countLabel.setFont(countLabel.getFont().deriveFont(Font.BOLD, countFontSize));
        countLabel.setAlignmentX(CENTER_ALIGNMENT);

        textLabel = new JLabel(text);
        textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD, textFontSize));
        textLabel.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(countLabel);
        add(textLabel);
        add(Box.createVerticalGlue());
    }

    public void setCount(int count) {
        countLabel.setText(count + "");
    }

    public void setText(String text) {
        textLabel.setText(text);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        int max = Math.max(size.width, size.height);
        return new Dimension(max, max);
    }

    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int size = Math.min(getWidth(), getHeight());
        // border
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, size, size, 180, 180);

        // background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(3, 3, size - 6, size - 6, 180, 180);

        g2.dispose();
        super.paintComponent(g);
    }
}
