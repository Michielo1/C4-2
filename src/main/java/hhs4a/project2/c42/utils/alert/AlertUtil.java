package hhs4a.project2.c42.utils.alert;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlertUtil {

    private static AlertUtil instance;

    private AlertUtil() {
    }

    public static AlertUtil getInstance() {
        if (instance == null) instance = new AlertUtil();
        return instance;
    }

    private final Image icon = new Image(Objects.requireNonNull(Objects.requireNonNull(AlertUtil.class.getResourceAsStream("/hhs4a/project2/c42/images/c4-2_bot.png"))));


    // Declare a static map to store the alerts
    private final Map<String, Alert> alertMap = new HashMap<>();

    public void showAlert(Alert.AlertType alertType, String title, String content) {
        try {
            // Generate a unique key for the alert based on its type and content
            String key = alertType.name() + ":" + content;

            // Check if an alert with the same key is already showing
            if (isAlertShowing(key)) {
                return; // Return early if an alert with the same key is already showing
            }

            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);

            // Set the icon for the Alert window
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(icon);

            // Add the alert to the map with its key
            addAlert(key, alert);

            // Show the alert and wait for user response
            alert.showAndWait();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {
            System.out.println("AlertUtil: " + e.getMessage());
        }
    }

    // A helper method to check if an alert with a given key is showing
    private boolean isAlertShowing(String key) {
        Alert existingAlert = alertMap.get(key);
        return existingAlert != null && existingAlert.isShowing();
    }

    // A helper method to add an alert to the map and set its close handler
    private void addAlert(String key, Alert alert) {
        // Put the alert in the map with its key
        alertMap.put(key, alert);

        // Remove the alert from the map when it is closed
        alert.setOnCloseRequest(e -> alertMap.remove(key));
    }
}