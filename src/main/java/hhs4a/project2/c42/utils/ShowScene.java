package hhs4a.project2.c42.utils;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ShowScene {
    public static void goToLoginScreen(Stage currentStage, FXMLLoader loader, Class<?> theClass) {
        try {
            Scene scene = new Scene(loader.load());
            Stage newStage = new Stage();

            newStage.setTitle("C4-2");
            newStage.setMinHeight(600);
            newStage.setMinWidth(800);

            scene.getStylesheets().add(Objects.requireNonNull(theClass.getResource("/hhs4a/project2/c42/css/LoginScreenCSS.css")).toExternalForm());

            newStage.getIcons().add(new Image(Objects.requireNonNull(theClass.getResourceAsStream("/hhs4a/project2/c42/images/c4-2_bot.png"))));

            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);

            newStage.setScene(scene);
            newStage.show();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}