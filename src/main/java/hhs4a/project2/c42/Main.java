package hhs4a.project2.c42;

import hhs4a.project2.c42.utils.css.CSSLoader;
import hhs4a.project2.c42.utils.css.CSSPaths;
import hhs4a.project2.c42.utils.internet.InternetConnection;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //Setting the title and the minimum size of the application
        stage.setTitle("C4-2 Login");
        stage.setMinHeight(600);
        stage.setMinWidth(800);

        //Applying a css file to the scene (javaFX has slightly different css syntax)
        scene.getStylesheets().add(CSSLoader.loadCSS(CSSPaths.LOGIN_SCREEN_CSS));

        // Code for setting an application icon
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("images/c4-2_bot.png"))));

        // Setting the themes for the MaterialFX components, this is new since the last time I used MaterialFX
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);

        stage.setScene(scene);
        stage.show();

        // Check internet
        InternetConnection.getInstance().internetCheck();
    }
}