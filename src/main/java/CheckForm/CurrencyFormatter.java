package CheckForm;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextField;
import javax.swing.Timer;

public class CurrencyFormatter {

    private Map<String, JTextField> textFieldMap = new HashMap<>();
    private Map<String, Integer> previousLengths = new HashMap<>(); 

    private Timer formatTimer;

    public CurrencyFormatter(Object formObject) {
        initTextFieldMap(formObject);
        startAutoUpdate();
    }

    private void initTextFieldMap(Object formObject) {
        Field[] fields = formObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == JTextField.class) {
                try {
                    field.setAccessible(true);
                    JTextField textField = (JTextField) field.get(formObject);
                    textFieldMap.put(field.getName(), textField);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startAutoUpdate() {
        formatTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldMap.forEach((name, textField) -> {
                    int currentLength = textField.getText().length();
                    Integer previousLength = previousLengths.get(name);

                    if (previousLength != null && currentLength == previousLength) {
                        return;
                    } else {
                        previousLengths.put(name, currentLength);
                        resetCurrencyFields();
                        formatCurrencyFields();
                    }
                });
            }
        });

        formatTimer.start();
    }

    public void formatCurrencyFields() {
        textFieldMap.forEach((name, textField) -> {
            if (name.contains("Money")) {
                String input = textField.getText().trim();
                
                if (isFormattedCorrectly(input)) {
                    return;
                }

                String formattedAmount = formatCurrency(input);
                textField.setText(formattedAmount);

                int textLength = textField.getText().length();
                if (textLength > 4) {
                    textField.setCaretPosition(textLength - 4);
                }
            }
        });
    }

    public void resetCurrencyFields() {
        textFieldMap.forEach((name, textField) -> {
            if (name.contains("Money")) {
                String input = textField.getText().trim();
                String resetAmount = resetCurrency(input);
                textField.setText(resetAmount);
            }
        });
    }

    public void resetAllCurrencyFields() {
        textFieldMap.forEach((name, textField) -> {
            String resetAmount = resetCurrency(textField.getText().trim());
            textField.setText(resetAmount);
        });
    }

    private String formatCurrency(String input) {
        input = input.replaceAll(" VND", "").trim();

        try {
            input = input.replaceAll(",", "").replaceAll("\\.", "");

            double amount = Double.parseDouble(input);
            String formattedAmount = String.format("%,.0f", amount);

            return formattedAmount + " VND";
        } catch (NumberFormatException e) {
            return input;
        }
    }

    private String resetCurrency(String input) {
        return input.replaceAll(",", "").replaceAll("\\.", "").replaceAll(" VND", "").trim();
    }

    private boolean isFormattedCorrectly(String input) {
        return input.matches("\\d{1,3}(,\\d{3})*(\\.\\d{2})? VND");
    }
}
