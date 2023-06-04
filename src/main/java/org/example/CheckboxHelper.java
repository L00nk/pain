package org.example;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class CheckboxHelper {
    private final JLabel label;
    private final JCheckBox checkBox;
    private final JTextField textField;

    public CheckboxHelper(String labelText) {
        label = new JLabel(labelText);
        checkBox = new JCheckBox();
        textField = new JTextField();
        textField.setEnabled(false);

        checkBox.addItemListener(e -> {
            textField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
        });
    }

    public JLabel getLabel() {
        return label;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public JTextField getTextField() {
        return textField;
    }

}
