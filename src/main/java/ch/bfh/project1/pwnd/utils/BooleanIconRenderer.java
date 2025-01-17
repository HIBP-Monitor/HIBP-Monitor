package ch.bfh.project1.pwnd.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class BooleanIconRenderer extends DefaultTableCellRenderer {
    private final Icon trueIcon = new ImageIcon(getClass().getResource("/icons/true.png"));
    private final Icon falseIcon = new ImageIcon(getClass().getResource("/icons/false.png"));

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(JLabel.CENTER);
        if (value instanceof Boolean) {
            label.setIcon((Boolean) value ? trueIcon : falseIcon);
            label.setText("");
        }
        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
        } else {
            label.setBackground(table.getBackground());
            label.setForeground(table.getForeground());
        }
        return label;
    }
}
