package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.Optional;

public final class AlertUtil {
    private AlertUtil() {
    }

    public static void showInfo(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showWarning(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    public static void showError(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static void showSqlError(SQLException exception) {
        showError("Database error", userFriendlySqlMessage(exception));
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static String userFriendlySqlMessage(SQLException exception) {
        String message = exception.getMessage();
        if (message == null) {
            return "An unexpected database error occurred.";
        }
        String lower = message.toLowerCase();
        if (lower.contains("duplicate")) {
            return "A record with the same unique information already exists.";
        }
        if (lower.contains("foreign key")) {
            return "This record is linked to other data and cannot be changed in that way.";
        }
        if (lower.contains("communications link failure") || lower.contains("connection")) {
            return "Unable to connect to MySQL. Check that the server is running and database.properties is correct.";
        }
        return message;
    }
}
