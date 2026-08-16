package com.mam;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

public class Main {
    static void run() {
        FlatIntelliJLaf.setup();
        try {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void main() {
        SwingUtilities.invokeLater(Main::run);
    }
}
