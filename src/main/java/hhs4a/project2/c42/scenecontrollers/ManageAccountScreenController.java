package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageAccountScreenController implements Initializable, Screencontroller {

    /*
        Variables
     */

    @FXML
    public BorderPane manageAccountScreenBorderPane;
    @FXML
    private MFXFilterComboBox<String> accountSelectorComboBox = new MFXFilterComboBox<>();
    @FXML
    private MFXButton permissionAndStatusButton;
    @FXML
    private MFXButton usernameButton;
    @FXML
    private MFXButton emailButton;
    @FXML
    private MFXButton passwordButton;
    @FXML
    private MFXButton accountDeleteButton;
    private List<Account> accounts;
    private static Account selectedAccount;

    /*
        Getters
     */

    public static Account getSelectedAccount() {
        return selectedAccount;
    }

    /*
        Initialize (gets called by javafx)
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Account database ophalen
        AbstractDatabaseUtilTemplate<Account> accountDatabaseUtil = AccountDatabaseUtil.getInstance();
        accounts = accountDatabaseUtil.getAccounts();

        // Voeg alle accounts toe aan de accountList
        ObservableList<String> accountList = FXCollections.observableArrayList();
        for (Account account : accounts) {
            accountList.add(account.getId() + ". " + account.getEmail());
        }
        accountSelectorComboBox.setItems(accountList);

        // Listener om account selectie te pakken
        accountSelectorComboBox.selectedItemProperty().addListener((observable, oldValue, newValue) -> handleAccountSelector(newValue));
    }

    public void handleAccountSelector(String newValue) {
        if (newValue != null) {
            int accountId = Integer.parseInt(newValue.split("\\.")[0].trim()); // Zoek de id

            // Loop for alle accounts en kijk welke is geselecteerd
            for (Account account : accounts) {
                if (account.getId() == accountId) {
                    selectedAccount = account;
                    AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Accountselectie", "U heeft het volgende account geselecteerd: '" + account.getEmail() + "'. U kunt nu bewerkingen uitvoeren voor dit account.");
                    break;
                }
            }
        } else {
            // Reset, want een gebruiker kan op 'cleared selection' klikken
            selectedAccount = null;
        }

        toggleButtonState();
    }

    /*
        Util methods
     */

    private void toggleButtonState() {
        if (selectedAccount != null) {
            permissionAndStatusButton.setDisable(false);
            usernameButton.setDisable(false);
            emailButton.setDisable(false);
            passwordButton.setDisable(false);
            accountDeleteButton.setDisable(false);
        } else {
            permissionAndStatusButton.setDisable(true);
            usernameButton.setDisable(true);
            emailButton.setDisable(true);
            passwordButton.setDisable(true);
            accountDeleteButton.setDisable(true);
        }
    }

    private void setScreen(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            manageAccountScreenBorderPane.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Button handlers - Not worth creating ActionHandlers
     */

    public void changePermissionAndStatusButtonClick() {
        setScreen("/hhs4a/project2/c42/fxml/AccountStatusAndPermissionScreen.fxml");
    }

    public void changePasswordButtonClick() {
        setScreen("/hhs4a/project2/c42/fxml/AccountPasswordScreen.fxml");
    }

    public void changeEmailButtonClick() {
        setScreen("/hhs4a/project2/c42/fxml/AccountEmailScreen.fxml");
    }

    public void changeUsernameButtonClick() {
        setScreen("/hhs4a/project2/c42/fxml/AccountUsernameScreen.fxml");
    }

    public void deleteAccountButtonClick() {
        setScreen("/hhs4a/project2/c42/fxml/DeleteAccountScreen.fxml");
    }

}