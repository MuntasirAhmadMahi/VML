package com.mam.ui.component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ScalableImageIcon extends ImageIcon {
    private final int width;
    private final int height;

    public ScalableImageIcon() {
        this.width = 120;
        this.height = 120;
    }

    public ScalableImageIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setImage(URL imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        doSetImage(image);
    }

    public void setImage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        doSetImage(image);
    }

    public void setImage(BufferedImage image) {
        doSetImage(image);
    }

    private void doSetImage(BufferedImage image) {
        int ow = image.getWidth(), oh = image.getHeight();
        /*
         ratioWidth = desiredMaximumWidth / ow
         ratioHeight = desiredMaximumHeight / oh
         pick the smallest ratio
         r = min(ratioWidth, rationHeight)
         */
        double r = Math.min((double) width / ow, (double) height / oh);
        int w = (int) Math.round(ow * r);
        int h = (int) Math.round(oh * r);
        super.setImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
}
