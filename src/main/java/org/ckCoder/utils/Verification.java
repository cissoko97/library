package org.ckCoder.utils;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verification {
    public static boolean numeric(String value) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean emailVerificatio(String email) {
        Pattern pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void dangerField(ComboBox comboBox) {
        comboBox.getStyleClass().add("form-input-error");
    }

    public static void dangerField(TextField comboBox) {
        comboBox.getStyleClass().add("form-input-error");
    }

    public static void dangerField(PasswordField comboBox) {
        comboBox.getStyleClass().add("form-input-error");
    }


    public static void dangerField(Button comboBox) {
        comboBox.getStyleClass().add("form-input-error");
    }

    public static void dangerField(TextArea comboBox) {
        comboBox.getStyleClass().add("form-input-error");
    }

    public static void dangerField(MenuButton menuButton) {
        menuButton.getStyleClass().add("form-input-error");
    }

    public static void remouveDangerClass(Node node) {
        node.getStyleClass().removeAll("form-input-error", "text-danger");
    }

    public static void alertMessage(List<String> message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < message.size(); i++) {
            text.append(i + 1).append(".  ").append(message.get(i)).append(" \n");
        }

        alert.setContentText(text.toString());
        System.out.printf(text.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();

    }

    public static void alertMessage(Map<String, String> errors, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        StringBuilder text = new StringBuilder();

        String[] keys = errors.keySet().toArray(new String[errors.size()]);
        for (int i = 0; i < keys.length; i++) {
            text.append(keys[i]).append(": ").append(errors.get(keys[i])).append(" \n");
        }

        alert.setContentText(text.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();

    }

    public static Alert alertMessage(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
        return alert;
    }

    public static Alert alertMessage(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        return alert;
    }
}
