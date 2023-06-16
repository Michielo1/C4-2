package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.accountstatusandpermissionsscreen.BlockAccountButtonClick;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.accountstatusandpermissionsscreen.ToggleButtonClick;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.accountstatusandpermissionsscreen.UnblockAccountButtonClick;
import hhs4a.project2.c42.utils.account.Account;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountStatusAndPermissionScreenController implements Initializable, Screencontroller {

    /*
        Variables
     */

    @FXML
    private MFXButton blockAccountButton;
    @FXML
    private MFXButton unblockAccountButton;
    @FXML
    private MFXToggleButton onlineChatbotToggle;
    @FXML
    private MFXToggleButton offlineChatbotToggle;
    @FXML
    private MFXToggleButton adminToggle;
    private Account account;

    /*
        Getters
     */

    public Account getAccount() {
        return account;
    }

    public MFXToggleButton getOnlineChatbotToggle() {
        return onlineChatbotToggle;
    }

    public MFXToggleButton getOfflineChatbotToggle() {
        return offlineChatbotToggle;
    }

    public MFXToggleButton getAdminToggle() {
        return adminToggle;
    }

    /*
        Initialize (gets called by javafx)
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account = ManageAccountScreenController.getSelectedAccount();
        updateBlockUnblockButtonState();
        updateToggleButtonState();
    }

    /*
        Util methods
     */

    public void updateBlockUnblockButtonState() {
        blockAccountButton.setDisable(account.getStatus());
        unblockAccountButton.setDisable(!account.getStatus());
    }

    public void updateToggleButtonState() {
        onlineChatbotToggle.setSelected(account.getPermissions().getCanChatWithOnlineChatbot());
        offlineChatbotToggle.setSelected(account.getPermissions().getCanChatWithOfflineChatbot());
        adminToggle.setSelected(account.getPermissions().isAdmin());
    }

    public void updateAccountPermissions(Object source) {
        // Bepaal welke togglebutton de event heeft geactiveerd
        if (source == onlineChatbotToggle) {
            account.getPermissions().setCanChatWithOnlineChatbot(onlineChatbotToggle.isSelected());
        } else if (source == offlineChatbotToggle) {
            account.getPermissions().setCanChatWithOfflineChatbot(offlineChatbotToggle.isSelected());
        } else if (source == adminToggle) {
            account.getPermissions().setAdmin(adminToggle.isSelected());
        }
    }

    /*
        Button handlers
     */

    public void onClickUnblockAccountButton() {
        UnblockAccountButtonClick unblockAccountButtonClick = new UnblockAccountButtonClick();
        unblockAccountButtonClick.setup(this);
        unblockAccountButtonClick.handle();
    }

    public void onClickBlockAccountButton() {
        BlockAccountButtonClick blockAccountButtonClick = new BlockAccountButtonClick();
        blockAccountButtonClick.setup(this);
        blockAccountButtonClick.handle();
    }

    public void onToggleButtonClick(ActionEvent event) {
        ToggleButtonClick toggleButtonClick = new ToggleButtonClick();
        toggleButtonClick.setup(this);
        toggleButtonClick.setEvent(event);
        toggleButtonClick.handle();
    }
}