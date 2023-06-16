package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen.*;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.chatscreen.*;
import hhs4a.project2.c42.utils.chatscreen.messagecell.MessageCell;
import hhs4a.project2.c42.utils.chatscreen.messagecell.MessageCellUserDeleteEditButtons;
import hhs4a.project2.c42.utils.database.databaseutils.*;
import hhs4a.project2.c42.utils.internet.InternetConnection;
import hhs4a.project2.c42.utils.message.BotMessage;
import hhs4a.project2.c42.utils.message.MessageInterface;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.virtualizedfx.cell.Cell;
import io.github.palexdev.virtualizedfx.controls.VirtualScrollPane;
import io.github.palexdev.virtualizedfx.enums.ScrollPaneEnums;
import io.github.palexdev.virtualizedfx.flow.VirtualFlow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatScreenController implements Initializable, Screencontroller {

    /*
        Variables
     */

    @FXML
    private MFXTextField subjectTextField;
    @FXML
    private MFXButton sendButton;
    @FXML
    private BorderPane chatBorderPane;
    @FXML
    private MFXTextField promptTextField;
    @FXML
    private MFXComboBox<String> languageSelectorComboBox = new MFXComboBox<>();
    @FXML
    private BorderPane chatHistoryOverlayLoaderBorderPane;
    @FXML
    private VBox chatScreenVBox;
    @FXML
    private VirtualScrollPane chatScrollPane;
    @FXML
    private MFXButton editSubjectButton;
    @FXML
    private MFXButton confirmEditSubjectButton;
    @FXML
    private MFXButton cancelEditSubjectButton;
    @FXML
    private MFXButton deleteConversationButton;
    @FXML
    private MFXButton newConversationButton;
    // Create an observable list of messages
    private static final ObservableList<MessageInterface> messages = FXCollections.observableArrayList();
    private AbstractDatabaseUtilTemplate<Chat> chatDatabaseUtil;
    private AbstractDatabaseUtilTemplate<ConversationHistory> historyDatabaseUtil;
    private AbstractDatabaseUtilTemplate<Question> questionDatabaseUtil;
    private AbstractDatabaseUtilTemplate<Answer> answerDatabaseUtil;
    private AbstractDatabaseUtilTemplate<Conversation> conversationDatabaseUtil;
    // putting the instance in a variable to reduce constructor chaining a bit
    private final ConversationHistoryHolder conversationHistoryHolder = ConversationHistoryHolder.getInstance();

    /*
        Getters
     */

    public MFXButton getSendButton() {
        return sendButton;
    }

    public MFXTextField getPromptTextField() {
        return promptTextField;
    }

    public MFXButton getNewConversationButton() {
        return newConversationButton;
    }

    public MFXButton getDeleteConversationButton() {
        return deleteConversationButton;
    }

    public MFXComboBox<String> getLanguageSelectorComboBox() {
        return languageSelectorComboBox;
    }

    public MFXTextField getSubjectTextField() {
        return subjectTextField;
    }

    public MFXButton getEditSubjectButton() {
        return editSubjectButton;
    }

    public MFXButton getCancelEditSubjectButton() {
        return cancelEditSubjectButton;
    }

    public MFXButton getConfirmEditSubjectButton() {
        return confirmEditSubjectButton;
    }

    public static ObservableList<MessageInterface> getMessages() {
        return messages;
    }

    /*
        Initialize (used by javafx)
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // empty all message and conversation first
        clearMessageAndConversation();

        //Gettind the database connections
        chatDatabaseUtil = ChatDatabaseUtil.getInstance();
        historyDatabaseUtil = HistoryDatabaseUtil.getInstance();
        questionDatabaseUtil = QuestionDatabaseUtil.getInstance();
        answerDatabaseUtil = AnswerDatabaseUtil.getInstance();
        conversationDatabaseUtil = ConversationDatabaseUtil.getInstance();


        //Set the language selector to the default language
        ObservableList<String> languageList = FXCollections.observableArrayList();
        languageList.add("English");
        languageList.add("Nederlands");
        languageSelectorComboBox.setItems(languageList);

        languageSelectorComboBox.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Language changed to: " + newValue + " with chatId: " + conversationHistoryHolder.getConversationHistory().getChat().getChatId() + " and AccountId: " + LoggedInAccountHolder.getInstance().getAccount().getId() + " and subject: " + conversationHistoryHolder.getConversationHistory().getChat().getSubject() + " and language: " + languageSelectorComboBox.getValue());
            conversationHistoryHolder.getConversationHistory().getChat().setLanguage(newValue);
            if(conversationHistoryHolder.getConversationHistory().getChat().getChatId() > 0) {
                //for switching between language immediately instead of after 60 seconds
                if (!InternetConnection.getInstance().isInternetAvailable() || !InternetConnection.getInstance().isAlpacaAvailableBool()) {
                    if (newValue.equalsIgnoreCase("English")) {
                        conversationHistoryHolder.getConversationHistory().getChat().setModel(Model.AIML_ENG);
                    } else if (newValue.equalsIgnoreCase("Nederlands")) {
                        conversationHistoryHolder.getConversationHistory().getChat().setModel(Model.AIML_NL);
                    }

                    System.out.println("Model changed to: " + conversationHistoryHolder.getConversationHistory().getChat().getModel());
                } else {
                    //TODO get the standard model from the database
                    //TODO turn on the translator
                    conversationHistoryHolder.getConversationHistory().getChat().setModel(Model.ALPACA13B);
                }

                chatDatabaseUtil.update(conversationHistoryHolder.getConversationHistory().getChat());
            }
        });

        // Create a virtual flow for the messages
        VirtualFlow<MessageInterface, Cell<MessageInterface>> chatFlow = new VirtualFlow<>(messages, new MessageCell());

        // Set the orientation of the chatFlow to vertical
        chatFlow.setOrientation(Orientation.VERTICAL);
        chatFlow.setCellSize(50); // Fixed cell height
        // Set the fit to breadth property of the chatFlow to true
        chatFlow.setFitToBreadth(true);

        // Set the chatScrollPane content to the chatFlow
        chatScrollPane = chatFlow.wrap();
        chatScrollPane.setVBarPolicy(ScrollPaneEnums.ScrollBarPolicy.DEFAULT);
        chatScrollPane.setVBarPos(ScrollPaneEnums.VBarPos.RIGHT);
        chatScrollPane.setSmoothScroll(true);
        chatScrollPane.getStyleClass().add("scroll-pane-fx-chat");
        BorderPane.setMargin(chatScrollPane, new Insets(15, 0, 15, 0));

        chatScrollPane.setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                double scrollAmount = chatScrollPane.getVVal() - deltaY / chatFlow.getHeight();
                chatScrollPane.setVVal(scrollAmount);
                event.consume();
        });

        // Set the chatScrollPane to the center of the chatBorderPane
        chatBorderPane.setCenter(chatScrollPane);

        updateUI();

        if (conversationHistoryHolder.getConversationHistory().getChat().getChatId() <= 0) {
            onNewConversationButtonClick();
        }

        // enter voor chat bericht versturen
        promptTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendButton.fire();
            }
        });
    }

    /*
        Util methods
     */

    public void loadChatHistory() {
        // Wis de lijst met berichten en het gesprek in de conversatiegeschiedenis
        clearMessageAndConversation();

        conversationHistoryHolder.getConversationHistory().getChat().setChatId(conversationHistoryHolder.getConversationHistory().getChat().getChatId());

        if(conversationHistoryHolder.getConversationHistory().getChat().getChatId() <= 0 || LoggedInAccountHolder.getInstance().getAccount().getId() <= 0) {
            return;
        }


        // Haal de gespreksgeschiedenis op uit de database, tijdelijk om te kijken of het leeg is of niet
        List<MessageInterface> conversations = conversationDatabaseUtil.getConversations(LoggedInAccountHolder.getInstance().getAccount(), conversationHistoryHolder.getConversationHistory().getChat());

        System.out.println("Conversations size: " + conversations.size());
        messages.addAll(conversations);
        conversationHistoryHolder.getConversationHistory().getConversation().addAll(messages);
    }

    public void sendMessageToChatbot(Question question) {
        // Setting the onAction event for the send button to the cancel button (cancel button is the same button as the send button)
        sendButton.setOnAction(actionEvent -> onCancelButtonClick());
        sendButton.setText("Vraag annuleren");

        MessageCellUserDeleteEditButtons.disableDeleteButtons();
        MessageCellUserDeleteEditButtons.disableEditButtons();


        Thread thread = new Thread(() ->{
            // Haal het antwoord op van de chatbot
            String response = ChatHandler.getInstance().handleUserInput(conversationHistoryHolder.getConversationHistory());

            // Als het antwoord leeg is, geef dan een standaard antwoord
            if(response == null || response.isBlank()) {
                if (languageSelectorComboBox.getValue().equals("English")) {
                    response = "Sorry, I can currently not answer this question.";
                } else if (languageSelectorComboBox.getValue().equals("Nederlands")) {
                    response = "Sorry, op dit moment kan ik deze vraag niet beantwoorden.";
                } else {
                    response = "Sorry, I don't understand.";
                }
            }

            if (response.equals("CANCELLED")) {
                if (languageSelectorComboBox.getValue().equals("English")) {
                    response = "Question has been cancelled!";
                } else {
                    response = "Vraag is geannuleerd!";
                }
            }

            // Voeg het antwoord toe aan de lijst met berichten, nadat het antwoord is opgehaald
            // Platform.runLater gaat weer op de gui thread
            String finalResponse = response;
            Platform.runLater(() -> {

                //Adding the answer and conversation to the database
                Answer answer = new Answer(finalResponse, -1);
                answer.setId(answerDatabaseUtil.add(answer));

                Conversation conversation = new Conversation(question, answer, conversationHistoryHolder.getConversationHistory().getChat(), -1);
                conversation.setId(conversationDatabaseUtil.add(conversation));

                // Create and add the bot message
                BotMessage botMessage = new BotMessage(answer);
                messages.add(botMessage);
                conversationHistoryHolder.getConversationHistory().getConversation().add(botMessage);

                // Enabling the buttons
                deleteConversationButton.setDisable(false);
                newConversationButton.setDisable(false);
                languageSelectorComboBox.setDisable(false);

                MessageCellUserDeleteEditButtons.enableDeleteButtons();
                MessageCellUserDeleteEditButtons.enableLastEditButton();

                // after the task/thread is done, set the send button back to the send button
                sendButton.setOnAction(actionEvent -> onSendButtonClick());
                sendButton.setText("Verstuur bericht");
            });

        });

        // Set the thread to a daemon thread so it will stop when the application is closed
        // It will also automatically stop when the thread is done
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void showChatHistoryOverlay(){
        AnchorPane pane;
        try {
            pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/hhs4a/project2/c42/fxml/ConversationHistoryOverlay.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(pane != null) {
            //loading in a new instance of the chat history overlay, most likely the best way to do it since we need the chat history to update
            chatHistoryOverlayLoaderBorderPane.setCenter(pane);
            //Disabling the chat screen so the user can't click on anything and enabling the chat history overlay
            chatScreenVBox.setDisable(true);
            chatHistoryOverlayLoaderBorderPane.setVisible(true);
            chatHistoryOverlayLoaderBorderPane.setDisable(false);
        } else {
            System.out.println("Pane is null");
        }
    }

    @FXML
    public void hideChatHistoryOverlay(){
        System.out.println("Hide chat history");
        //Disabling and hiding the chat history overlay so the user can't click on anything and re-enabling the chat screen
        chatHistoryOverlayLoaderBorderPane.setVisible(false);
        chatHistoryOverlayLoaderBorderPane.setDisable(true);
        chatScreenVBox.setDisable(false);
        MenuOverlayScreenController.chatHistoryOverlayOpen = false;

        //remove the chat history overlay from the borderpane
        if (chatHistoryOverlayLoaderBorderPane.getChildren().size() > 0) {
            chatHistoryOverlayLoaderBorderPane.getChildren().remove(0);
        }
    }

    public void subjectFieldListener() {
        // Listener om te controleren welke button getoond moet worden
        subjectTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                cancelEditSubjectButton.setDisable(false);
                cancelEditSubjectButton.setVisible(true);
                confirmEditSubjectButton.setDisable(true);
                confirmEditSubjectButton.setVisible(false);
            } else {
                cancelEditSubjectButton.setDisable(true);
                cancelEditSubjectButton.setVisible(false);
                confirmEditSubjectButton.setDisable(false);
                confirmEditSubjectButton.setVisible(true);
            }
        });
    }

    public void enableButtonsExpectConfirmCancel() {
        promptTextField.setEditable(true);
        enableButtons();
        editSubjectButton.setDisable(false);
        editSubjectButton.setVisible(true);
        confirmEditSubjectButton.setDisable(true);
        confirmEditSubjectButton.setVisible(false);
        cancelEditSubjectButton.setDisable(true);
        cancelEditSubjectButton.setVisible(false);
    }

    public void disableButtons() {
        sendButton.setDisable(true);
        editSubjectButton.setDisable(true);
        confirmEditSubjectButton.setDisable(true);
        cancelEditSubjectButton.setDisable(true);
        deleteConversationButton.setDisable(true);
        newConversationButton.setDisable(true);
        languageSelectorComboBox.setDisable(true);
    }

    private void enableButtons() {
        if(conversationHistoryHolder.getConversationHistory().getChat().getChatId() > 0) {
            chatBorderPane.getCenter().setDisable(false);
            sendButton.setDisable(false);
            deleteConversationButton.setDisable(false);
            languageSelectorComboBox.setDisable(false);
        }
        editSubjectButton.setDisable(false);
        confirmEditSubjectButton.setDisable(false);
        cancelEditSubjectButton.setDisable(false);
        newConversationButton.setDisable(false);
    }

    //code that gets executed on conversation start and switches
    public void updateUI(){

        loadChatHistory();

        // Set subject
        subjectTextField.setText(conversationHistoryHolder.getConversationHistory().getChat().getSubject());

        // Language is saved as lowercase in the database, but the combobox expects the first letter as uppercase
        char firstLetter = conversationHistoryHolder.getConversationHistory().getChat().getLanguage().charAt(0);
        languageSelectorComboBox.setValue(Character.toUpperCase(firstLetter) + conversationHistoryHolder.getConversationHistory().getChat().getLanguage().substring(1));
        languageSelectorComboBox.setText(languageSelectorComboBox.getValue());

        //if no messages are found, add the starting question
        if (messages.isEmpty()) {
            System.out.println("No messages found");
            addStartingQuestion();
        }

        //if the chatid is 0, disable the chat. 0 is the standard value aka when no chat
        if(conversationHistoryHolder.getConversationHistory().getChat().getChatId() <= 0){
            chatScrollPane.setDisable(true);
            newConversationButton.setDisable(false);
            editSubjectButton.setDisable(true);
            subjectTextField.setEditable(false);
            sendButton.setDisable(true);
            deleteConversationButton.setDisable(true);
        } else {
            chatScrollPane.setDisable(false);
            newConversationButton.setDisable(false);
            editSubjectButton.setDisable(false);
            editSubjectButton.setVisible(true);
            subjectTextField.setEditable(false);
            sendButton.setDisable(false);
            deleteConversationButton.setDisable(false);
        }

        //Set the chat history overlay to invisible
        hideChatHistoryOverlay();
    }

    public void clearMessageAndConversation() {
        messages.clear();
        conversationHistoryHolder.getConversationHistory().getConversation().clear();
    }

    //Code for adding the standard how can I help you bot message to the top
    public void addStartingQuestion(){
        if (languageSelectorComboBox.getValue().equalsIgnoreCase("English")) {
            messages.add(new BotMessage(new Answer("Hello, how can I help you?", -60)));
        } else if (languageSelectorComboBox.getValue().equalsIgnoreCase("Nederlands")) {
            messages.add(new BotMessage(new Answer("Hallo, hoe kan ik u helpen?", -60)));
        }
    }

    public void resetButtons(){
        // Pas de zichtbaarheid en uitgeschakeld status van de knoppen aan
        editSubjectButton.setDisable(false);
        editSubjectButton.setVisible(true);
        confirmEditSubjectButton.setDisable(true);
        confirmEditSubjectButton.setVisible(false);
        cancelEditSubjectButton.setDisable(true);
        cancelEditSubjectButton.setVisible(false);
        // Schakel bewerking van het onderwerptekstveld uit
        subjectTextField.setEditable(false);
    }

    /*
        Button handlers - Have to be in this class due to javafx
     */

    public void onSendButtonClick() {
        SendButtonClick sendButtonClick = new SendButtonClick();
        sendButtonClick.setup(this);
        sendButtonClick.handle();
    }

    public void onCancelButtonClick() {
        CancelButtonClick cancelButtonClick = new CancelButtonClick();
        cancelButtonClick.setup(this);
        cancelButtonClick.handle();
    }

    public void onDeleteConversationButtonClick() {
        DeleteConversationButtonClick deleteConversationButtonClick = new DeleteConversationButtonClick();
        deleteConversationButtonClick.setup(this);
        deleteConversationButtonClick.handle();
    }

    public void onEditConversationButtonClick() {
        EditConversationButtonClick editConversationButtonClick = new EditConversationButtonClick();
        editConversationButtonClick.setup(this);
        editConversationButtonClick.handle();
    }

    public void onNewConversationButtonClick() {
        NewConversationButtonClick newConversationButtonClick = new NewConversationButtonClick();
        newConversationButtonClick.setup(this);
        newConversationButtonClick.handle();
    }

    public void onCancelEditSubjectButton(String oldSubject) {
        CancelEditSubjectButtonClick cancelEditSubjectButtonClick = new CancelEditSubjectButtonClick();
        cancelEditSubjectButtonClick.setup(this);
        cancelEditSubjectButtonClick.setOldSubject(oldSubject);
        cancelEditSubjectButtonClick.handle();
    }

}