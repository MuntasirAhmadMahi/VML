package com.mam.ui.component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CharacterCountTextField extends JTextField {
    public static int NUMBER = 0;
    public static int TEXT = 1;

    private final JLabel counterLabel;

    // attributes
    private final int inputType;
    private final int maxChars;
    private final boolean canBeEmpty;

    public CharacterCountTextField(int maxChars, int inputType, String hint, boolean canBeEmpty) {
        this.maxChars = Math.max(maxChars, 0);
        this.inputType = inputType;
        this.canBeEmpty = canBeEmpty;

        counterLabel = new JLabel("(0/" + maxChars + ")");
        counterLabel.setForeground(UIManager.getColor("textInactiveText"));

        putClientProperty("JTextField.trailingComponent", counterLabel);
        putClientProperty("JTextField.placeholderText", hint);

        if (!canBeEmpty) {
            putClientProperty("JComponent.outline", "error"); // initially it is empty
        }

        addFilter();
        addCounterUpdater();
    }

    private boolean isValidInput(String input) {
        if (inputType == NUMBER) {
            return input.matches("\\d+");
        }
        return true;
    }

    private void addFilter() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null || !isValidInput(string)) {
                    return;
                }
                if ((fb.getDocument().getLength() + string.length()) <= maxChars) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null || !isValidInput(text)) {
                    return;
                }
                int textLength = fb.getDocument().getLength() - length + text.length();
                if (textLength <= maxChars) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private void addCounterUpdater() {
        getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                int length = getText().length();
                counterLabel.setText("(" + length + "/" + maxChars + ")");
                if (canBeEmpty) {
                    return;
                }
                if (length == 0) {
                    putClientProperty("JComponent.outline", "error");
                } else {
                    putClientProperty("JComponent.outline", null);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
    }

    public boolean isEmpty() {
        return getText().isEmpty();
    }
}
