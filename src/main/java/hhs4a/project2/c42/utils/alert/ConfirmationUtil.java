package hhs4a.project2.c42.utils.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class ConfirmationUtil {
    private final Image icon = new Image(Objects.requireNonNull(Objects.requireNonNull(AlertUtil.class.getResourceAsStream("/hhs4a/project2/c42/images/c4-2_bot.png"))));

    private static ConfirmationUtil instance;

    private ConfirmationUtil(){}

    public static ConfirmationUtil getInstance(){
        if(instance == null) instance = new ConfirmationUtil();
        return instance;
    }

    public Optional<ButtonType> showConfirmation(String title, String content) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);

            // Set the icon for the Alert window
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(icon);

            // Maak buttons aan, zodat er "ja" en "nee" worden getoond
            ButtonType jaButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
            ButtonType neeButton = new ButtonType("Nee", ButtonBar.ButtonData.NO);

            // Voeg ze toe als button
            alert.getButtonTypes().setAll(jaButton, neeButton);

            // Wachten tot dat de gebruiker op de button heeft geklikt
            return alert.showAndWait();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {
            System.out.println("AlertUtil: " + e.getMessage());
            return Optional.empty();
        }
    }
}
