package CheckForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ResetForm {

    private Map<String, JTextField> textFieldMap = new HashMap<>();
    private Map<String, JComboBox<?>> comboBoxMap = new HashMap<>();
    private Map<String, JRadioButton> radioButtonMap = new HashMap<>();
    private Map<String, JCheckBox> checkBoxMap = new HashMap<>();
    private JButton resetButton;

    public void initComponentMap(Object formObject) {
        Field[] fields = formObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.getType() == JTextField.class) {
                    JTextField textField = (JTextField) field.get(formObject);
                    textFieldMap.put(field.getName(), textField);
                } else if (field.getType() == JComboBox.class) {
                    JComboBox<?> comboBox = (JComboBox<?>) field.get(formObject);
                    comboBoxMap.put(field.getName(), comboBox);
                } else if (field.getType() == JRadioButton.class) {
                    JRadioButton radioButton = (JRadioButton) field.get(formObject);
                    radioButtonMap.put(field.getName(), radioButton);
                } else if (field.getType() == JCheckBox.class) {
                    JCheckBox checkBox = (JCheckBox) field.get(formObject);
                    checkBoxMap.put(field.getName(), checkBox);
                } else if (field.getType() == JButton.class) {
                    JButton button = (JButton) field.get(formObject);
                    if ("Reset".equals(field.getName())) {
                        resetButton = button;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void attachResetButtonListener() {
        if (resetButton != null) {
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetComponents();
                }
            });
        }
    }

     public void resetComponents() {
        textFieldMap.forEach((name, textField) -> textField.setText(""));
        comboBoxMap.forEach((name, comboBox) -> comboBox.setSelectedIndex(0));
        radioButtonMap.forEach((name, radioButton) -> radioButton.setSelected(true));
        checkBoxMap.forEach((name, checkBox) -> checkBox.setSelected(false));
    }

}
