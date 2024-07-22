package CheckForm;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 * A custom cell renderer for JTable that hides password fields with asterisks.
 */
public class Hide_Password extends JTextField implements TableCellRenderer {

    public Hide_Password() {
        setOpaque(true);
        setHorizontalAlignment(JTextField.LEFT);
        setEditable(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Ensure value is a String
        if (value instanceof String) {
            String password = (String) value;
            String maskedPassword = "*".repeat(password.length());
            setText(maskedPassword);
        } else {
            setText(""); // Clear text if value is not a string
        }
        
        // Set background and foreground color based on selection
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        
        return this;
    }
}
