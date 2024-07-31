package CheckForm;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ImageRenderer extends JLabel implements TableCellRenderer {

    public ImageRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER); // Căn giữa hình ảnh theo chiều ngang
        setVerticalAlignment(CENTER);   // Căn giữa hình ảnh theo chiều dọc
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            ImageIcon originalIcon = new ImageIcon((String) value);
            Image image = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Thay đổi kích thước hình ảnh
            ImageIcon scaledIcon = new ImageIcon(image);
            setIcon(scaledIcon);
        } else {
            setIcon(null);
        }
        return this;
    }
}
