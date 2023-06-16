package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.enums.SortOption;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.conversationhistoryoverlayscreen.DeleteAllConversationsButtonClick;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.historyoverlay.ConversationHistoryCell;
import hhs4a.project2.c42.utils.historyoverlay.HistoryOverlayDatabaseHandler;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.virtualizedfx.cell.Cell;
import io.github.palexdev.virtualizedfx.controls.VirtualScrollPane;
import io.github.palexdev.virtualizedfx.enums.ScrollPaneEnums;
import io.github.palexdev.virtualizedfx.flow.VirtualFlow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConversationHistoryOverlayController implements Initializable, Screencontroller {

    /*
        Variables
     */

    @FXML
    private BorderPane chatHistoryScreenBorderPane;
    @FXML
    private MFXComboBox<String> sortChatComboBox = new MFXComboBox<>();

    private SortOption sortOption = SortOption.DEFAULT;
    private static final ObservableList<ConversationHistory> chatHistory = FXCollections.observableArrayList();
    public static ObservableList<ConversationHistory> getChatHistory() {
        return chatHistory;
    }

    /*
        Initialize (gets called by javafx)
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Laad alles in
        loadChatHistory();

        // Voeg de verschillende sorteer opties toe
        ObservableList<String> sortChatOptions = FXCollections.observableArrayList(SortOption.DEFAULT.getLabel(), SortOption.SUBJECT_ASC.getLabel(), SortOption.SUBJECT_DESC.getLabel(), SortOption.LAST_ACTIVE_ASC.getLabel(), SortOption.LAST_ACTIVE_DESC.getLabel());
        sortChatComboBox.setItems(sortChatOptions);
        sortChatComboBox.getSelectionModel().selectFirst(); // Selecteer de eerste optie

        // Listener toegevoegd om te detecteren welke sorteer optie er is gekozen
        sortChatComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> handleSortChatSelector(SortOption.values()[newValue.intValue()]));

        // Create a virtual flow of messages
        VirtualFlow<ConversationHistory, Cell<ConversationHistory>> chatHistoryFlow = createChatHistoryFlow();
        // Wrap the flow in a virtual scrollpane
        VirtualScrollPane chatHistoryVirtualScrollpane = createChatHistoryVirtualScrollPane(chatHistoryFlow);

        chatHistoryVirtualScrollpane.setOnScroll(event -> {
            chatHistoryVirtualScrollpane.setVVal(chatHistoryVirtualScrollpane.getVVal() - event.getDeltaY() / chatHistoryFlow.getHeight());
            event.consume();
        });

        //setting the scrollpane to the borderpane
        chatHistoryScreenBorderPane.setCenter(chatHistoryVirtualScrollpane);
    }

    private void loadChatHistory() {
        chatHistory.clear();
        chatHistory.addAll(HistoryOverlayDatabaseHandler.getInstance().getConversationHistories(sortOption));
        System.out.println("ChatHistorySize: " + chatHistory.size());
    }

    private VirtualFlow<ConversationHistory, Cell<ConversationHistory>> createChatHistoryFlow() {
        VirtualFlow<ConversationHistory, Cell<ConversationHistory>> chatHistoryFlow = new VirtualFlow<>(chatHistory, new ConversationHistoryCell());
        chatHistoryFlow.setOrientation(Orientation.VERTICAL);
        chatHistoryFlow.setCellSize(50);
        chatHistoryFlow.setFitToBreadth(true);
        return chatHistoryFlow;
    }

    private VirtualScrollPane createChatHistoryVirtualScrollPane(VirtualFlow<ConversationHistory, Cell<ConversationHistory>> chatHistoryFlow) {
        VirtualScrollPane chatHistoryVirtualScrollpane = chatHistoryFlow.wrap();
        chatHistoryVirtualScrollpane.setVBarPolicy(ScrollPaneEnums.ScrollBarPolicy.DEFAULT);
        chatHistoryVirtualScrollpane.setVBarPos(ScrollPaneEnums.VBarPos.RIGHT);
        chatHistoryVirtualScrollpane.setSmoothScroll(true);
        chatHistoryVirtualScrollpane.getStyleClass().add("scroll-pane-fx-history");
        BorderPane.setMargin(chatHistoryVirtualScrollpane, new Insets(0, 0, 15, 0));
        return chatHistoryVirtualScrollpane;
    }

    private void handleSortChatSelector(SortOption newValue) {
        if (newValue != null) {
            sortOption = newValue;
            System.out.println("History wordt nu gesorteerd op: " + sortOption.getLabel());
            chatHistory.clear(); // Maak de vorige chatgeschiedenis leeg
            chatHistory.addAll(HistoryOverlayDatabaseHandler.getInstance().getConversationHistories(sortOption));
        } else {
            System.out.println("Essex. Lemon.");
        }
    }

    /*
        Util methods
     */

    public void deleteAllConversationsButtonClick() {
        DeleteAllConversationsButtonClick deleteAllConversationsButtonClick = new DeleteAllConversationsButtonClick();
        deleteAllConversationsButtonClick.setup(this);
        deleteAllConversationsButtonClick.handle();
    }

    public void deleteConversationsAndClearChatHistory(List<Chat> chats) {
        HistoryOverlayDatabaseHandler.getInstance().deleteConversations(chats);
        // Clear chat history
        chatHistory.clear();
        ConversationHistoryHolder.getInstance().getConversationHistory().setChat(new Chat());
        MenuOverlayScreenController.chatScreenController.updateUI();
        MenuOverlayScreenController.chatScreenController.onNewConversationButtonClick();
    }
}