package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.enums.ThemeType;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.settingsscreen.FullscreenButtonClick;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.settingsscreen.ManageAccountButtonClick;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.settingsscreen.RegisterAccountButtonClick;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.account.Settings;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.SettingsDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsScreenController implements Initializable, Screencontroller {

    /*
        Variables
     */

    @FXML
    private BorderPane settingScreenBorderPane;
    @FXML
    private MFXButton manageAccountButton;
    @FXML
    private MFXButton registerAccountButton;
    @FXML
    private MFXComboBox<Model> onlineModelSelectorComboBox = new MFXComboBox<>();
    @FXML
    private MFXComboBox<DriverEnum> driverSelectorComboBox = new MFXComboBox<>();
    @FXML
    private MFXComboBox<String> themeSelectorComboBox = new MFXComboBox<>();
    @FXML
    private MFXButton fullscreenButton;
    private final AbstractDatabaseUtilTemplate<Settings> settingsDatabaseUtil = SettingsDatabaseUtil.getInstance();

    /*
        Getters
     */

    public MFXButton getFullscreenButton() {
        return fullscreenButton;
    }

    /*
        Initialize method (gets called by javafx on screen load)
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Vullen van de comboboxes met items
        ObservableList<Model> modelList = FXCollections.observableArrayList(Model.ALPACA13B, Model.ALPACA7B, Model.AIML_NL, Model.AIML_ENG, Model.AUTOMATISCH);
        onlineModelSelectorComboBox.setItems(modelList);

        ObservableList<DriverEnum> driverList = FXCollections.observableArrayList(DriverEnum.CHROME, DriverEnum.EDGE, DriverEnum.FIREFOX, DriverEnum.OPERA, DriverEnum.AUTOMATISCH);
        driverSelectorComboBox.setItems(driverList);

        ObservableList<String> themeList = FXCollections.observableArrayList(ThemeType.DARK.getType(), ThemeType.LIGHT.getType());
        themeSelectorComboBox.setItems(themeList);

        // Controleer of model en driver niet null zijn
        checkAndSetModel();
        checkAndSetDriver();
        checkAndSetTheme();

        // Disable manage account based on permission
        disableAccountAdmin();

        // Listener om te bepalen of er iets is gewijzigd in de combobox
        onlineModelSelectorComboBox.selectedItemProperty().addListener((observable, oldValue, newValue) -> handleModelSelector(oldValue, newValue));
        driverSelectorComboBox.selectedItemProperty().addListener((observable, oldValue, newValue) -> handleDriverSelector(oldValue, newValue));
        themeSelectorComboBox.selectedItemProperty().addListener((observable, oldValue, newValue) -> handleThemeSelector(oldValue, newValue));
    }

    /*
        Util methods
     */

    public void checkAndSetModel() {
        if (ConversationHistoryHolder.getInstance().getConversationHistory().getModel() != null) {
            // Set the current chat model
            try {
                onlineModelSelectorComboBox.getSelectionModel().selectItem(ConversationHistoryHolder.getInstance().getConversationHistory().getModel());
            } catch (IllegalArgumentException e) {
                onlineModelSelectorComboBox.setValue(null);
            }
        }
    }

    public void checkAndSetDriver() {
        if (LoggedInAccountHolder.getInstance().getAccount().getSettings().getDiverOption() != null) {
            // Set Driver
            driverSelectorComboBox.getSelectionModel().selectItem(DriverEnum.valueOf(LoggedInAccountHolder.getInstance().getAccount().getSettings().getDiverOption().name()));
        }
    }

    public void checkAndSetTheme() {
        if (LoggedInAccountHolder.getInstance().getAccount().getSettings().getTheme() != null) {
            // Set the current theme
            themeSelectorComboBox.getSelectionModel().selectItem(LoggedInAccountHolder.getInstance().getAccount().getSettings().getTheme().getType());
        }
    }

    public void disableAccountAdmin() {
        if (!LoggedInAccountHolder.getInstance().getAccount().getPermissions().isAdmin()) {
            manageAccountButton.setDisable(true);
            registerAccountButton.setDisable(true);
        }
    }

    public boolean checkChatbotPermissions(Model newModel) {
        if (!LoggedInAccountHolder.getInstance().getAccount().getPermissions().getCanChatWithOfflineChatbot() && (newModel == Model.AIML_NL || newModel == Model.AIML_ENG)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Geen toestemming voor offline chatbot", "U heeft geen toestemming om te chatten met de offline chatbot.");
            onlineModelSelectorComboBox.setValue(null);
            return false;
        }

        if (!LoggedInAccountHolder.getInstance().getAccount().getPermissions().getCanChatWithOnlineChatbot() && (newModel == Model.ALPACA13B || newModel == Model.ALPACA7B)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Geen toestemming voor online chatbot", "U heeft geen toestemming om te chatten met de online chatbot.");
            onlineModelSelectorComboBox.setValue(null);
            return false;
        }

        return true;
    }

    public void handleModelSelector(Model oldModel, Model newModel) {
        if (ConversationHistoryHolder.getInstance().getConversationHistory().getChat().getChatId() > 0) {

            if (!checkChatbotPermissions(newModel)) return;

            ConversationHistoryHolder.getInstance().getConversationHistory().getChat().setModel(newModel);

            if (settingsDatabaseUtil.update(LoggedInAccountHolder.getInstance().getAccount().getSettings())) {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Succesvol chatmodel selectie gewijzigd", "Het chatmodel is succesvol aangepast naar '" + onlineModelSelectorComboBox.getValue() + "'.");
            }
        } else {
            // Reset Model selectie
            onlineModelSelectorComboBox.setValue(oldModel);
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij het wijzigen van het chatmodel", "Het chatmodel kan niet worden aangepast. U kunt alleen het chatmodel aanpassen wanneer u een actieve chat heeft.");
        }
    }

    public void handleDriverSelector(DriverEnum oldDriver, DriverEnum newDriver) {
        if (LoggedInAccountHolder.getInstance().getAccount().getId() > 0) {

            LoggedInAccountHolder.getInstance().getAccount().getSettings().setDiverOption(newDriver);

            if (settingsDatabaseUtil.update(LoggedInAccountHolder.getInstance().getAccount().getSettings())) {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Succesvol driver selectie gewijzigd", "De driver is succesvol aangepast naar '" + driverSelectorComboBox.getValue() + "'.");
            }
        } else {
            // Reset Driver selectie
            driverSelectorComboBox.setValue(oldDriver);
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij het wijzigen van de driver", "De driver kan niet worden aangepast. Probeer het alstublieft opnieuw.");
        }
    }

    public void handleThemeSelector(String oldTheme, String newTheme) {
        if (LoggedInAccountHolder.getInstance().getAccount().getId() > 0) {
            for (ThemeType themeType : ThemeType.values()) {
                if (themeType.getType().equalsIgnoreCase(newTheme)) {
                    LoggedInAccountHolder.getInstance().getAccount().getSettings().setTheme(themeType);
                    if (settingsDatabaseUtil.update(LoggedInAccountHolder.getInstance().getAccount().getSettings())) {
                        AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Succesvol", "Het thema is succesvol aangepast naar '" + themeSelectorComboBox.getValue() + "'. " + "Log opnieuw in om de wijzigingen toe te passen.");
                    } else {
                        // Reset Driver selectie
                        themeSelectorComboBox.setValue(oldTheme);
                        AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij het wijzigen van het thema", "Het thema kan niet worden aangepast. Probeer het alstublieft opnieuw.");
                    }
                }
            }
        }
    }

    public void setScreen(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            settingScreenBorderPane.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Button handlers
     */

    public void manageAccountButtonClick() {
        ManageAccountButtonClick manageAccountButtonClick = new ManageAccountButtonClick();
        manageAccountButtonClick.setup(this);
        manageAccountButtonClick.handle();
    }

    public void registerAccountButtonClick() {
        RegisterAccountButtonClick registerAccountButtonClick = new RegisterAccountButtonClick();
        registerAccountButtonClick.setup(this);
        registerAccountButtonClick.handle();
    }

    // Maak de stage fullscreen modues
    public void fullscreenButtonClick() {
        FullscreenButtonClick fullscreenButtonClick = new FullscreenButtonClick();
        fullscreenButtonClick.setup(this);
        fullscreenButtonClick.handle();
    }
}