package CheckForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatter {

    private Map<String, JTextField> textFieldMap = new HashMap<>();
    private Map<String, Integer> previousLengths = new HashMap<>();

    private Timer formatTimer;

    public DateFormatter(Object formObject) {
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
        formatTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldMap.forEach((name, textField) -> {
                    int currentLength = textField.getText().length();
                    Integer previousLength = previousLengths.get(name);

                    if (previousLength != null && currentLength == previousLength) {
                        return;
                    } else {
                        previousLengths.put(name, currentLength);
                        
                        formatDateFields();
                    }
                });
            }
        });

        formatTimer.start();
    }

    public void formatDateFields() {
        textFieldMap.forEach((name, textField) -> {
            if (name.contains("Date")) {
                String input = textField.getText().trim();

                if (isFormattedCorrectly(input)) {
                    return;
                }

                String formattedDate = formatDate(input);
                textField.setText(formattedDate);

                
            }
        });
    }

    private String formatDate(String input) {
        input = input.replaceAll("[^\\d]", ""); // Remove non-digit characters

        if (input.length() != 8) {
            return input; // Return input if it's not in the expected length
        }

        try {
            String year = input.substring(0, 4);
            String month = input.substring(4, 6);
            String day = input.substring(6, 8);

            String formattedDate = String.format("%s-%s-%s", year, month, day);

            // Validate the date
            if (isValidDate(formattedDate)) {
                return formattedDate;
            } else {
                return "Ngày không hợp lệ"; // Invalid date
            }
        } catch (Exception e) {
            return "Ngày không hợp lệ"; // Invalid date
        }
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isFormattedCorrectly(String input) {
        return input.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
