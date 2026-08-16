package com.mam.ui.layout;

import java.awt.*;

public class VerticalFillLayout implements LayoutManager {
    private final int hGap;
    private final int vGap;

    public VerticalFillLayout() {
        this(0, 0);
    }

    public VerticalFillLayout(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(parent);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int x = insets.left + hGap, y = insets.top;
        int nComps = parent.getComponentCount();
        int availWidth = parent.getWidth() - insets.left - insets.right - 2 * hGap;

        for (int i = 0; i < nComps; i++) {
            Component c = parent.getComponent(i);
            if (!c.isVisible())
                continue;

            Dimension d = c.getMinimumSize();
            // Set the component's size and position
            c.setBounds(x, y + vGap, availWidth, d.height);
            y += d.height + 2 * vGap;
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "[hGap = " + hGap + ", vGap = " + vGap + "]";
    }

    private Dimension getLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();

        // Add container's insets.
        Insets insets = parent.getInsets();
        int height = insets.top + insets.bottom;

        for (int i = 0; i < nComps; i++) {
            Component c = parent.getComponent(i);
            if (!c.isVisible()) {
                continue;
            }

            Dimension d = c.getMinimumSize();
            height += d.height + 2 * vGap;
        }

        return new Dimension(0, height);
    }
}
