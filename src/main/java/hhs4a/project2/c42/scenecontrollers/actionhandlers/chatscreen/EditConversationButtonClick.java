package hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen;

import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.database.databaseutils.ChatDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.Alert;

public class EditConversationButtonClick extends ChatscreenHandler implements Actionhandler {

    @Override
    public void setup(Screencontroller screencontroller) {
        super.setup(screencontroller);
    }

    @Override
    public void handle() {
        MFXTextField subjectTextField = controller.getSubjectTextField();
        String oldSubject = subjectTextField.getText();
        subjectTextField.setEditable(true);
        subjectTextField.requestFocus();
        subjectTextField.positionCaret(subjectTextField.getLength());

        // Hide de bewerken knop
        controller.getEditSubjectButton().setDisable(true);
        controller.getEditSubjectButton().setVisible(false);
        // Maak de annuleren button eerst zichtbaar
        controller.getCancelEditSubjectButton().setDisable(false);
        controller.getCancelEditSubjectButton().setVisible(true);

        controller.subjectFieldListener();

        controller.getConfirmEditSubjectButton().setOnAction(e -> {
            // check of onderwerp leeg is
            if (subjectTextField.getText().isBlank()){
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Info", "Onderwerp mag niet leeg zijn.");
                subjectTextField.setText(oldSubject);
                return;
            }

            ConversationHistoryHolder.getInstance().getConversationHistory().getChat().setSubject(subjectTextField.getText());


            // aangepaste onderwerp bijwerken in database
            if (ChatDatabaseUtil.getInstance().update(ConversationHistoryHolder.getInstance().getConversationHistory().getChat())) {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Info", "Onderwerp is succesvol bewerkt.");
                controller.resetButtons();
                // set subject
                ConversationHistoryHolder.getInstance().getConversationHistory().setChat(ConversationHistoryHolder.getInstance().getConversationHistory().getChat());
            } else {
                AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Error", "Onderwerp kon niet bewerkt worden.");
                ConversationHistoryHolder.getInstance().getConversationHistory().getChat().setSubject(oldSubject);
            }
        });

        controller.getCancelEditSubjectButton().setOnAction(e -> {
            System.out.println("Cancel edit subject with chatid " + ConversationHistoryHolder.getInstance().getConversationHistory().getChat().getChatId());
            if(ConversationHistoryHolder.getInstance().getConversationHistory().getChat().getChatId() != 0) {
                subjectTextField.setText(oldSubject);
                controller.resetButtons();
            } else {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Geen gespreksonderwerp ingevuld", "Voer een gespreksonderwerp in om te beginnen met chatten.");
            }
        });
    }
}
