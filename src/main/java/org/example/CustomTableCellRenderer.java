package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private List<Integer> sameRows;
    private List<Integer> editedRows;
    private int editedRow = 0;

    public CustomTableCellRenderer(List<Integer> sameRows, List<Integer> editedRows) {
        this.sameRows = sameRows;
        this.editedRows = editedRows;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (sameRows.contains(row)) {
            c.setBackground(Color.RED);
        }
        else {
            c.setBackground(Color.GRAY);
        }
        if (editedRows.contains(row)) {
            c.setBackground(Color.WHITE);
        }

        if (editedRows.size() > 0 && editedRows.contains(row)) {
            c.setBackground(Color.WHITE);
        }
        return c;
    }
}
