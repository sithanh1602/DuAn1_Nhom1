package CheckForm;
import javax.swing.table.DefaultTableCellRenderer;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CurrencyRenderer extends DefaultTableCellRenderer {
    private NumberFormat currencyFormat;

    public CurrencyRenderer() {
        currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        setHorizontalAlignment(RIGHT); // Align text to the right
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            value = currencyFormat.format(((Number) value).doubleValue());
        }
        super.setValue(value);
    }
}
