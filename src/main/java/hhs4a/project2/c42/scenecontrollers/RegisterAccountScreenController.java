package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.registerscreen.RegisterButtonClick;
import hhs4a.project2.c42.utils.account.Permission;
import hhs4a.project2.c42.utils.account.Settings;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.PermissionDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.SettingsDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public class RegisterAccountScreenController implements Screencontroller {

    /*
        Variables
     */

    @FXML
    private MFXTextField usernameTextField;
    @FXML
    private MFXTextField emailTextField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private MFXPasswordField confirmPasswordField;
    @FXML
    private MFXToggleButton statusToggle;
    @FXML
    private MFXToggleButton onlineChatbotToggle;
    @FXML
    private MFXToggleButton offlineChatbotToggle;
    @FXML
    private MFXToggleButton adminToggle;
    private final AbstractDatabaseUtilTemplate<Settings> settingsDatabaseUtil = SettingsDatabaseUtil.getInstance();
    private final AbstractDatabaseUtilTemplate<Permission> permissionDatabaseUtil = PermissionDatabaseUtil.getInstance();

    /*
        Getters
     */

    public MFXTextField getUsernameTextField() {
        return usernameTextField;
    }

    public MFXTextField getEmailTextField() {
        return emailTextField;
    }

    public MFXPasswordField getPasswordField() {
        return passwordField;
    }

    public MFXPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public MFXToggleButton getStatusToggle() {
        return statusToggle;
    }

    /*
        Util methods
     */

    public Settings createAndSaveSettings() {
        Settings settings = new Settings();
        settings.setId(settingsDatabaseUtil.add(settings));
        return settings;
    }

    public Permission createAndSavePermissions() {
        Permission permissions = new Permission();
        permissions.setCanChatWithOnlineChatbot(onlineChatbotToggle.isSelected());
        permissions.setCanChatWithOfflineChatbot(offlineChatbotToggle.isSelected());
        permissions.setAdmin(adminToggle.isSelected());
        permissions.setId(permissionDatabaseUtil.add(permissions));
        return permissions;
    }

    public void resetRegisterFields() {
        usernameTextField.clear();
        emailTextField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        // Toggle button resetten
        onlineChatbotToggle.setSelected(false);
        offlineChatbotToggle.setSelected(false);
        adminToggle.setSelected(false);
        statusToggle.setSelected(false);
    }

    public boolean checkIfFieldsAreBlank() {
        ArrayList<MFXTextField> textFields = new ArrayList<>();
        textFields.add(usernameTextField);
        textFields.add(emailTextField);
        textFields.add(passwordField);
        textFields.add(confirmPasswordField);

        for (MFXTextField textField : textFields) {
            if (textField.getText().isBlank()) {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Velden zijn leeg", "Vul alstublieft alle velden in.");
                return true;
            }
        }
        return false;
    }

    /*
        Button handlers
     */

    public void onRegisterClick() {
        RegisterButtonClick registerButtonClick = new RegisterButtonClick();
        registerButtonClick.setup(this);
        registerButtonClick.handle();
    }
}