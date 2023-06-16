package hhs4a.project2.c42.utils.chatscreen.messagecell;

import hhs4a.project2.c42.scenecontrollers.ChatScreenController;
import hhs4a.project2.c42.scenecontrollers.MenuOverlayScreenController;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.chatscreen.Answer;
import hhs4a.project2.c42.utils.chatscreen.Conversation;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.chatscreen.Question;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AnswerDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.ConversationDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.QuestionDatabaseUtil;
import hhs4a.project2.c42.utils.fx.ChangeButtonVisibility;
import hhs4a.project2.c42.utils.message.BotMessage;
import hhs4a.project2.c42.utils.message.MessageInterface;
import hhs4a.project2.c42.utils.message.UserMessage;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import java.util.Objects;
import java.util.Optional;

public class MessageCellUpdateUser implements MessageCellUpdateInterface {
    private String beforeEdit;
    private final AbstractDatabaseUtilTemplate<Answer> answerDatabaseUtil;
    private final AbstractDatabaseUtilTemplate<Question> questionDatabaseUtil;
    private final AbstractDatabaseUtilTemplate<Conversation> conversationDatabaseUtil;
    private final Account account;

    public MessageCellUpdateUser(Account account){
        answerDatabaseUtil = AnswerDatabaseUtil.getInstance();
        questionDatabaseUtil = QuestionDatabaseUtil.getInstance();
        conversationDatabaseUtil = ConversationDatabaseUtil.getInstance();
        this.account = account;
    }



    //TODO figure out why it is also enabling the first edit button when messagecell is reused

    public void updateMessageCell(MessageCellFX messageCellFX, MessageInterface message){
        //setting the background color
        //We need to remove the old styling before adding the new one. If we do .clear() it doesnt want to apply the new styling for some reason.
        BotMessage botMessage = new BotMessage(new Answer(""));
        messageCellFX.getMessageTextArea().getStyleClass().removeAll(botMessage.getSenderColor());
        messageCellFX.getMessageTextArea().getStyleClass().add(message.getSenderColor());

        //initializing the fields
        MessageCellUserDeleteEditButtons.addDeleteButton(messageCellFX.getDeleteButton());
        MessageCellUserDeleteEditButtons.addEditButton(messageCellFX.getEditButton());

        //Setting the profile picture, using ternary operator to check if the account is null. This is to reduce lines of code (to get sigrid code higher)
        messageCellFX.getImageView().setImage(new Image(Objects.requireNonNull(getClass().getResource(account != null ? account.getSettings().getProfilePicturePath() : "/hhs4a/project2/c42/images/clodsire.jpg")).toExternalForm()));

        // Knoppen aan/uitzetten
        messageCellFX.getEditButton().setVisible(true);
        ChangeButtonVisibility.showButton(new MFXButton[]{messageCellFX.getDeleteButton()});
        ChangeButtonVisibility.hideButton(new MFXButton[]{messageCellFX.getSaveButton(), messageCellFX.getCancelButton(), messageCellFX.getCopyButton()});
        MessageCellUserDeleteEditButtons.enableLastEditButton();

        // Actie voor het bewerkingsknop
        messageCellFX.getEditButton().setOnAction(event -> handleEditButtonAction(messageCellFX));

        // Set an action handler for the delete button
        messageCellFX.getDeleteButton().setOnAction(event -> handleDeleteButtonAction(messageCellFX, message));

        // Actie voor de opslaan knop
        messageCellFX.getSaveButton().setOnAction(event -> handleSaveButtonAction(messageCellFX, message));

        // Actie voor de annuleringsknop
        messageCellFX.getCancelButton().setOnAction(event -> handleCancelButtonAction(messageCellFX));
    }

    private void handleEditButtonAction(MessageCellFX messageCellFX) {
        if (!messageCellFX.getMessageTextArea().isEditable()) {
            MenuOverlayScreenController.chatScreenController.getSendButton().setDisable(true);
            // Het originele bericht opslaan voordat het wordt bewerkt
            beforeEdit = messageCellFX.getMessageTextArea().getText();
            // Zichtbaarheid en uitgeschakeld status van de knoppen aanpassen
            ChangeButtonVisibility.showButton(new MFXButton[]{messageCellFX.getSaveButton(), messageCellFX.getCancelButton()});
            ChangeButtonVisibility.hideButton(new MFXButton[]{messageCellFX.getEditButton(), messageCellFX.getDeleteButton()});
            // Bewerking van het berichttekstveld inschakelen en stijl aanpassen
            messageCellFX.getMessageTextArea().setEditable(true);
            messageCellFX.getMessageTextArea().requestFocus();
            messageCellFX.getMessageTextArea().positionCaret(messageCellFX.getMessageTextArea().getLength());
        }
    }

    private void handleDeleteButtonAction(MessageCellFX messageCellFX, MessageInterface message) {
        try {
            // Als gebruiker error krijgt met verwijderen van bericht, dan is er optie om chat opnieuw te herladen.
            // Dus voer wat eronder gebeurd niet. Pas als gebruiker geen error krijgt, verwijder het bericht.
            if (errorCheckDeleteMessage()) {
                return;
            }

            deleteQuestionAndAnswer(message);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No corresponding bot/user message found");
        }

        MessageCellUserDeleteEditButtons.getDeleteButtons().remove(messageCellFX.getDeleteButton());
        MessageCellUserDeleteEditButtons.getEditButtons().remove(messageCellFX.getEditButton());
        MessageCellUserDeleteEditButtons.enableLastEditButton();
    }

    private void deleteQuestionAndAnswer(MessageInterface message) {
        int index = ChatScreenController.getMessages().indexOf(message);
        // Maak alle objecten aan en vul ze in
        Question question = new Question(message.getText(), message.getId());
        Answer answer = new Answer("", answerDatabaseUtil.getAnswerId(question));
        Conversation conversation = new Conversation(question, answer, ConversationHistoryHolder.getInstance().getConversationHistory().getChat(), -1);
        // Verwijder ze uit database
        conversationDatabaseUtil.remove(conversation);
        questionDatabaseUtil.remove(question);
        answerDatabaseUtil.remove(answer);
        // Ook in conversationHistory en messages
        ChatScreenController.getMessages().remove(index + 1);
        ConversationHistoryHolder.getInstance().getConversationHistory().getConversation().remove(index + 1);
        ChatScreenController.getMessages().remove(message);
        ConversationHistoryHolder.getInstance().getConversationHistory().getConversation().remove(message);
        // Toon een alert als laatst
        AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Verwijdering gelukt", "De vraag is succesvol verwijderd.");
    }

    private void handleSaveButtonAction(MessageCellFX messageCellFX, MessageInterface message) {
        MenuOverlayScreenController.chatScreenController.getSendButton().setDisable(false);
        // If the input is blank, reject it
        if (messageCellFX.getMessageTextArea().getText().isBlank()){
            messageCellFX.getMessageTextArea().setText(beforeEdit);
        } else {
            saveUpdatedMessage(messageCellFX, message);
        }
    }

    private void handleCancelButtonAction(MessageCellFX messageCellFX) {
        // Herstel de tekst naar de oorspronkelijke waarde
        messageCellFX.getMessageTextArea().setText(beforeEdit);
        hideButtonsAndDisableMessageTextArea(messageCellFX);
        MenuOverlayScreenController.chatScreenController.getSendButton().setDisable(false);
    }

    private boolean errorCheckDeleteMessage() {
        // Simpele error controle toegevoegd voordat een vraag en antwoord worden verwijderd.
        // Het kan gebeuren dat een antwoord van de chatbot niet correct wordt opgeslagen. Dit heeft gevolgen voor hoe een vraag wordt verwijderd.
        // Omdat conversationHistory en Messages niet meer gelijk zijn aan elkaar kan er een indexOutOfBound error onstaan bij het verwijderen van een vraag.
        // Dit kan worden voorkomen om eerst te kijken of de size() wel hetzelfde is, anders toon een foutmelding dat de gebruiker de chat opnieuw moet herladen.
        // Door de gebruiker opnieuw te vragen om op de chat tabblad te klikken, wordt de chat en de conversaties opnieuw opgehaald en alleen de juiste getoond, zonder de errors.
        // Als het goed is moet dit eventuele foutmeldingen bij een bericht verwijderen oplossen. Trust me bro
        if (ChatScreenController.getMessages().size() != ConversationHistoryHolder.getInstance().getConversationHistory().getConversation().size()) {
            Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Foutmelding bericht verwijderen", "Het geselecteerde bericht kan niet worden verwijderd. Wilt u de chat herladen om dit probleem op te lossen?");

            if (result.isPresent() && result.get().getText().equals("Ja")) {
                MenuOverlayScreenController.chatScreenController.updateUI();
            }
            return true;
        }
        return false;
    }

    private void saveUpdatedMessage(MessageCellFX messageCellFX, MessageInterface message) {
        // Haal de nieuwe berichttekst op
        String newMessageText = messageCellFX.getMessageTextArea().getText();
        // Kijkt of de vraag gewijzigd is
        if (!newMessageText.equalsIgnoreCase(beforeEdit)) {
            Question newQuestion = new Question(newMessageText, message.getId());
            if (questionDatabaseUtil.update(newQuestion)) {
                // setText in messageTextArea
                messageCellFX.getMessageTextArea().setText(newMessageText);
                // update messages in lijst
                updateConversationHistoryAndMessages(message, newQuestion);
                // Zet de knoppen weer uit
                hideButtonsAndDisableMessageTextArea(messageCellFX);
                // verwijder oude bot message and genereer nieuw bot message
                deleteAndGenerateNewBotMessage(newQuestion);

                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Vraag gewijzigd", "De vraag is succesvol gewijzigd.");
            }
        } else {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Geen wijzigen", "Er zijn geen wijzigingen aangebracht in de tekst.");
        }
    }

    private void updateConversationHistoryAndMessages(MessageInterface message, Question newQuestion) {
        // Werk het bericht bij in de lijst met berichten
        ChatScreenController.getMessages().remove(ChatScreenController.getMessages().indexOf(message) + 1);
        ChatScreenController.getMessages().set(ChatScreenController.getMessages().indexOf(message), new UserMessage(newQuestion));
        ConversationHistoryHolder.getInstance().getConversationHistory().getConversation().clear();
        ConversationHistoryHolder.getInstance().getConversationHistory().getConversation().addAll(ChatScreenController.getMessages());
    }

    private void deleteAndGenerateNewBotMessage(Question newQuestion) {
        Answer oldBotMessage = new Answer("", answerDatabaseUtil.getAnswerId(newQuestion));
        Conversation conversation = new Conversation(newQuestion, oldBotMessage, ConversationHistoryHolder.getInstance().getConversationHistory().getChat(), -1);
        // Delete bot message, conversation and generate new message
        conversationDatabaseUtil.remove(conversation);
        answerDatabaseUtil.remove(oldBotMessage);
        MenuOverlayScreenController.chatScreenController.sendMessageToChatbot(newQuestion);
    }

    //Hide the buttons and disable the message text area
    private void hideButtonsAndDisableMessageTextArea(MessageCellFX messageCellFX){
        // Pas de zichtbaarheid en uitgeschakeld status van de knoppen aan
        ChangeButtonVisibility.hideButton(new MFXButton[]{messageCellFX.getSaveButton(), messageCellFX.getCancelButton()});
        ChangeButtonVisibility.showButton(new MFXButton[]{messageCellFX.getEditButton(), messageCellFX.getDeleteButton()});
        // Schakel bewerking van het bericht tekstveld uit en pas de stijl aan
        messageCellFX.getMessageTextArea().setEditable(false);
    }
}