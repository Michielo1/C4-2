package hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.database.databaseutils.ChatDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.HistoryDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Date;

public class NewConversationButtonClick extends ChatscreenHandler implements Actionhandler {


    @Override
    public void setup(Screencontroller screencontroller) {
        super.setup(screencontroller);
    }

    @Override
    public void handle() {
        ConversationHistoryHolder conversationHistoryHolder = ConversationHistoryHolder.getInstance();
        MFXTextField subjectTextField = controller.getSubjectTextField();

        // Reset chat id, zodat je niet kan annuleren van gespreksonderwerp wanneer je een nieuwe chat maakt
        conversationHistoryHolder.getConversationHistory().getChat().setChatId(-1);

        // Disable promptTextField and all buttons except for the confirm and cancel buttons
        controller.getPromptTextField().setEditable(false);
        controller.disableButtons();

        String oldSubject = subjectTextField.getText();

        subjectTextField.setEditable(true);
        subjectTextField.requestFocus();
        subjectTextField.clear();

        // Hide de bewerken knop
        controller.getEditSubjectButton().setDisable(true);
        controller.getEditSubjectButton().setVisible(false);
        // Maak de annuleren button eerst zichtbaar
        controller.getCancelEditSubjectButton().setDisable(false);
        controller.getCancelEditSubjectButton().setVisible(true);

        controller.subjectFieldListener();

        controller.getConfirmEditSubjectButton().setOnAction(e -> {
            String newSubject = subjectTextField.getText();

            // Onderwerp naam was niet leeg zijn
            if (!newSubject.trim().isEmpty()) {

                // Conversatie legen
                controller.clearMessageAndConversation();
                // Nieuwe chat id generen
                Model modelName = conversationHistoryHolder.getConversationHistory().getChat().getModel();
                conversationHistoryHolder.getConversationHistory().setChat(new Chat(-1, newSubject, modelName, "", controller.getLanguageSelectorComboBox().getValue()));
                conversationHistoryHolder.getConversationHistory().getChat().setChatId(ChatDatabaseUtil.getInstance().add(conversationHistoryHolder.getConversationHistory().getChat()));
                System.out.println("New Chat id: " + conversationHistoryHolder.getConversationHistory().getChat().getChatId());

                conversationHistoryHolder.getConversationHistory().setConversationHistory(new ConversationHistory(conversationHistoryHolder.getConversationHistory().getChat(), new ArrayList<>(), new Date(), -1));
                conversationHistoryHolder.getConversationHistory().setId(HistoryDatabaseUtil.getInstance().add(conversationHistoryHolder.getConversationHistory()));
                System.out.println(conversationHistoryHolder.getConversationHistory().getChat().getChatId());

                // Enable promptTextField and all buttons except for the confirm and cancel buttons
                controller.enableButtonsExpectConfirmCancel();

                subjectTextField.setEditable(false);
                System.out.println("New chat");
                controller.addStartingQuestion();
            } else {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Info", "Het onderwerp mag niet leeg zijn.");
            }
        });

        controller.getCancelEditSubjectButton().setOnAction((e -> controller.onCancelEditSubjectButton(oldSubject)));
    }
}
