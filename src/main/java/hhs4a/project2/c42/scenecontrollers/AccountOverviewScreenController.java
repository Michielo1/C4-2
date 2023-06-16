package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.accountoverviewscreen.EditProfileButtonClick;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.account.Settings;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.SettingsDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountOverviewScreenController implements Initializable, Screencontroller {

    /*
        Variables
     */

    @FXML
    private BorderPane accountBorderPane;
    @FXML
    private Text usernameText;
    @FXML
    private MFXButton changeUsernameButton;
    @FXML
    private Text emailText;
    @FXML
    private MFXButton changeEmailButton;
    @FXML
    private Text passwordText;
    @FXML
    private MFXButton changePasswordButton;
    @FXML
    private ImageView profileImageView;
    private Account account;

    /*
        Getters
     */

    public Account getAccount() {
        return account;
    }

    /*
        Initialize (gets called by javafx)
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        account = LoggedInAccountHolder.getInstance().getAccount();
        usernameText.setText(account.getUsername());
        emailText.setText(account.getEmail());
        passwordText.setText(account.getPassword().replaceAll(".", "*"));
        profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(account.getSettings().getProfilePicturePath())).toExternalForm()));

        if (!LoggedInAccountHolder.getInstance().getAccount().getPermissions().isAdmin()) {
            changeUsernameButton.setDisable(true);
            changeEmailButton.setDisable(true);
            changePasswordButton.setDisable(true);
        }
    }

    /*
        Button handlers
     */

    public void onEditProfileClick() {
        EditProfileButtonClick editProfileButtonClick = new EditProfileButtonClick();
        editProfileButtonClick.setup(this);
        editProfileButtonClick.handle();
    }

    public Stage createImageSelectionStage() {
        Stage imageSelectionStage = new Stage();
        imageSelectionStage.setTitle("Selecteer uw profielfoto");
        imageSelectionStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/hhs4a/project2/c42/images/c4-2_bot.png")).toExternalForm()));
        return imageSelectionStage;
    }

    public FlowPane createFlowPane() {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(15);
        flowPane.setVgap(15);
        flowPane.setPadding(new Insets(15));
        flowPane.setOrientation(Orientation.HORIZONTAL);
        return flowPane;
    }

    public File[] loadImagesFromResources() {
        String imagesPath = Objects.requireNonNull(getClass().getResource("/hhs4a/project2/c42/images")).getPath();

        if (imagesPath.contains("%20")) {
            imagesPath = imagesPath.replaceAll("%20", " ");
        }

        File imagesFolder = new File(imagesPath);
        return imagesFolder.listFiles();
    }

    public void displayImagesInFlowPane(File[] imageFiles, FlowPane flowPane, Stage imageSelectionStage) {
        if (imageFiles == null) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Afbeeldingen niet gevonden", "De afbeeldingen konden niet worden gevonden.");
            return;
        }

        for (File imageFile : imageFiles) {
            ImageView imageView = createImageViewWithRoundedBorders(imageFile);
            VBox imageContainer = createImageContainer(imageView, imageFile);
            imageView.setOnMouseClicked(event -> handleProfileChangeAction(imageFile, imageSelectionStage));
            flowPane.getChildren().add(imageContainer);
        }
    }

    private ImageView createImageViewWithRoundedBorders(File imageFile) {
        // Rectangle voor rounded borders
        Rectangle clip = new Rectangle(100, 100);
        clip.setArcWidth(8);
        clip.setArcHeight(8);
        // Image view
        ImageView imageView = new ImageView(imageFile.toURI().toString());
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setClip(clip);
        return imageView;
    }

    private Label createImageNameLabel(File imageFile) {
        // Label zodat te lange namen ingekord worden weergegeven
        Label imageName = new Label();
        imageName.setMaxWidth(100);
        imageName.setWrapText(false);
        imageName.setEllipsisString("...");
        imageName.setText(imageFile.getName());
        return imageName;
    }

    private VBox createImageContainer(ImageView imageView, File imageFile) {
        VBox imageContainer = new VBox();
        imageContainer.setMinHeight(100);
        imageContainer.setMinWidth(100);
        imageContainer.setAlignment(Pos.CENTER_LEFT);
        // add all
        imageContainer.getChildren().add(imageView);
        imageContainer.getChildren().add(createImageNameLabel(imageFile));

        return imageContainer;
    }

    private void handleProfileChangeAction(File imageFile, Stage imageSelectionStage) {
        String imagePath = imageFile.getPath();
        Path path = Paths.get(imagePath);
        String relativePath = "/hhs4a/project2/c42/images/" + path.getFileName().toString();

        Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Profielfoto bevestiging", "Wilt u '" + imageFile.getName() + "' als profielfoto selecteren?");
        if (result.isPresent() && result.get().getText().equals("Ja")) {
            account.getSettings().setProfilePicturePath(relativePath); // Update the profile picture path in the Account object
            AbstractDatabaseUtilTemplate<Settings> settingsDatabaseUtil = SettingsDatabaseUtil.getInstance();
            settingsDatabaseUtil.update(account.getSettings()); // Save new path of profile pic to the database.
            imageSelectionStage.close();
            initialize(null, null);
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Profielfoto is gewijzigd", "Uw profielfoto is succesvol bijgewerkt.");
        }
    }

    // not worth creating actionhandlers for these
    public void onEditUsernameClick() {
        screenSwitcher("/hhs4a/project2/c42/fxml/AccountUsernameScreen.fxml");
    }

    public void onEditEmailClick() {
        screenSwitcher("/hhs4a/project2/c42/fxml/AccountEmailScreen.fxml");
    }

    public void onEditPasswordClick() {
        screenSwitcher("/hhs4a/project2/c42/fxml/AccountPasswordScreen.fxml");
    }

    /*
        Util methods
     */

    private void screenSwitcher(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            accountBorderPane.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}