package hhs4a.project2.c42.utils.historyoverlay;

import hhs4a.project2.c42.enums.ThemeType;
import hhs4a.project2.c42.scenecontrollers.ConversationHistoryOverlayController;
import hhs4a.project2.c42.scenecontrollers.MenuOverlayScreenController;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.chatscreen.*;
import hhs4a.project2.c42.utils.chatscreen.messagecell.MessageCellUserDeleteEditButtons;
import hhs4a.project2.c42.utils.css.CSSStyling;
import hhs4a.project2.c42.utils.database.databaseutils.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.virtualizedfx.cell.Cell;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ConversationHistoryCell implements Function<ConversationHistory, Cell<ConversationHistory>> {
    private MFXButton deleteButton;
    private MFXButton openButton;
    private Label subjectLabel;
    private Label dateLabel;

    @Override
    public Cell<ConversationHistory> apply(ConversationHistory conversationHistory) {
        deleteButton = new MFXButton("Verwijderen");
        openButton = new MFXButton("Openen");
        subjectLabel = new Label();
        dateLabel = new Label();

        // Datum
        StackPane dateStackPane = new StackPane();
        dateStackPane.setAlignment(Pos.CENTER_LEFT);
        dateStackPane.setMinWidth(150);
        dateStackPane.setMaxWidth(Double.MAX_VALUE);
        dateStackPane.setMinHeight(45);
        dateStackPane.getChildren().add(dateLabel);
        dateLabel.getStyleClass().add("conversation-history-label");

        // Onderwerp
        StackPane subjectStackPane = new StackPane();
        subjectStackPane.setAlignment(Pos.CENTER_LEFT);
        subjectStackPane.setMinWidth(300);
        subjectStackPane.setMaxWidth(Double.MAX_VALUE);
        subjectStackPane.setMinHeight(45);
        subjectStackPane.getChildren().add(subjectLabel);
        subjectLabel.getStyleClass().add("conversation-history-label");
        subjectLabel.setMaxWidth(300);
        subjectLabel.setWrapText(false);
        subjectLabel.setEllipsisString("...");

        // Creating button
        openButton.setMinWidth(80);
        openButton.setMinHeight(40);
        deleteButton.setMinWidth(120);
        deleteButton.setMinHeight(40);

        // set button style en zichtbaarheid
        openButton.setVisible(true);
        deleteButton.setVisible(true);
        if (LoggedInAccountHolder.getInstance().getAccount().getSettings().getTheme() == ThemeType.LIGHT) {
            openButton.setStyle(CSSStyling.CONVERSATION_HISTORY_OPEN_BUTTON_LIGHT);
            deleteButton.setStyle(CSSStyling.CONVERSATION_HISTORY_DELETE_BUTTON_LIGHT);
        }
        if (LoggedInAccountHolder.getInstance().getAccount().getSettings().getTheme() == ThemeType.DARK) {
            openButton.setStyle(CSSStyling.CONVERSATION_HISTORY_OPEN_BUTTON_DARK);
            deleteButton.setStyle(CSSStyling.CONVERSATION_HISTORY_DELETE_BUTTON_DARK);
        }

        //openButton.getStyleClass().add("conversation-history-open-button"); can't use getStyle due to MFXManager
        //deleteButton.getStyleClass().add("conversation-history-delete-button"); can't use getStyle due to MFXManager

        // Creating gridpane
        GridPane chatHistoryGrid = new GridPane();
        chatHistoryGrid.add(dateStackPane, 0, 0);
        chatHistoryGrid.add(subjectStackPane, 1, 0);
        chatHistoryGrid.add(openButton, 2, 0);
        chatHistoryGrid.add(deleteButton, 3, 0);
        chatHistoryGrid.setHgap(10);
        chatHistoryGrid.setAlignment(Pos.CENTER_LEFT);

        // Setting column constraints
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.SOMETIMES);
        cc1.setFillWidth(true);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        cc2.setFillWidth(true);
        ColumnConstraints cc3 = new ColumnConstraints();
        cc3.setHgrow(Priority.NEVER);
        cc3.setFillWidth(true);
        ColumnConstraints cc4 = new ColumnConstraints();
        cc4.setHgrow(Priority.NEVER);
        cc4.setFillWidth(true);
        chatHistoryGrid.getColumnConstraints().addAll(cc1, cc2, cc3, cc4);

        updateCell(conversationHistory);

        return new Cell<>() {
            @Override
            public Node getNode() {
                return chatHistoryGrid;
            }

            @Override
            public void updateItem(ConversationHistory conversationHistory) {
                dateLabel.setText(conversationHistory.getFormattedDate());
                subjectLabel.setText(conversationHistory.getChat().getSubject());
                //updateCell(conversationHistory);
            }
        };
    }

    private void updateCell(ConversationHistory conversationHistory){

        AbstractDatabaseUtilTemplate<ConversationHistory> historyDatabaseUtil = HistoryDatabaseUtil.getInstance();
        AbstractDatabaseUtilTemplate<Chat> chatDatabaseUtil = ChatDatabaseUtil.getInstance();
        AbstractDatabaseUtilTemplate<Conversation> conversationDatabaseUtil = ConversationDatabaseUtil.getInstance();
        AbstractDatabaseUtilTemplate<Question> questionDatabaseUtil = QuestionDatabaseUtil.getInstance();
        AbstractDatabaseUtilTemplate<Answer> answerDatabaseUtil = AnswerDatabaseUtil.getInstance();

        dateLabel.setText(conversationHistory.getFormattedDate());
        subjectLabel.setText(conversationHistory.getChat().getSubject());

        openButton.setOnAction(actionEvent -> {
            // Clearing the arraylist of the messageCell
            MessageCellUserDeleteEditButtons.clearDeleteAndEditButtons();
            // updating the date from the conversationHistory
            conversationHistory.setDate(new Date(System.currentTimeMillis()));
            // Setting the conversationHistory in the chatScreenController
            ConversationHistoryHolder.getInstance().setConversationHistory(conversationHistory);
            MenuOverlayScreenController.chatScreenController.updateUI();
            MenuOverlayScreenController.chatScreenController.onCancelEditSubjectButton(conversationHistory.getChat().getSubject());
        });

        deleteButton.setOnAction(actionEvent -> {
            Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Chat verwijderen bevestiging", "Weet u zeker dat u de geselecteerde chat wilt verwijderen?");
            if (result.isPresent() && result.get().getText().equals("Ja")) {
                List<Conversation> conversations = conversationDatabaseUtil.getConversations(conversationHistory.getChat());
                historyDatabaseUtil.remove(conversationHistory);
                chatDatabaseUtil.remove(conversationHistory.getChat());
                for (Conversation conversation : conversations) {
                    conversationDatabaseUtil.remove(conversation);
                    questionDatabaseUtil.remove(conversation.getQuestion());
                    answerDatabaseUtil.remove(conversation.getAnswer());
                }

                ConversationHistoryOverlayController.getChatHistory().remove(conversationHistory);
                if (conversationHistory.getChat().getChatId() == ConversationHistoryHolder.getInstance().getConversationHistory().getChat().getChatId() || ConversationHistoryHolder.getInstance().getConversationHistory().getChat().getChatId() == 0) {
                    MenuOverlayScreenController.chatScreenController.updateUI();
                    MenuOverlayScreenController.chatScreenController.onNewConversationButtonClick();
                }
            }
        });
    }
}