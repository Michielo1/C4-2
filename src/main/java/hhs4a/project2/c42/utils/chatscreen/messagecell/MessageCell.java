package hhs4a.project2.c42.utils.chatscreen.messagecell;

import hhs4a.project2.c42.enums.ThemeType;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.css.CSSStyling;
import hhs4a.project2.c42.utils.message.MessageInterface;
import io.github.palexdev.virtualizedfx.cell.Cell;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;

import java.util.function.Function;

public class MessageCell implements Function<MessageInterface, Cell<MessageInterface>> {
    private Account account;

    // Creating the cell
    @Override
    public Cell<MessageInterface> apply(MessageInterface message) {
        final MessageCellFX messageCellFX = new MessageCellFX();


        // Create a text field to display the message text
        messageCellFX.getMessageTextArea().setEditable(false);
        messageCellFX.getMessageTextArea().setMinWidth(300);
        messageCellFX.getMessageTextArea().setMinHeight(50);
        // Set the maximum width and alignment of the text area
        messageCellFX.getMessageTextArea().setMaxWidth(Double.MAX_VALUE);

        // Create edit, delete, copy, save and cancel buttons
        messageCellFX.getEditButton().setMinWidth(75);
        messageCellFX.getEditButton().setMaxWidth(Double.MAX_VALUE);
        messageCellFX.getEditButton().setMinHeight(50);
        messageCellFX.getDeleteButton().setMinWidth(75);
        messageCellFX.getDeleteButton().setMaxWidth(Double.MAX_VALUE);
        messageCellFX.getDeleteButton().setMinHeight(50);
        messageCellFX.getCopyButton().setMinWidth(75);
        messageCellFX.getCopyButton().setMaxWidth(Double.MAX_VALUE);
        messageCellFX.getCopyButton().setMinHeight(50);
        messageCellFX.getSaveButton().setMinWidth(75);
        messageCellFX.getSaveButton().setMaxWidth(Double.MAX_VALUE);
        messageCellFX.getSaveButton().setMinHeight(50);
        messageCellFX.getCancelButton().setMinWidth(75);
        messageCellFX.getCancelButton().setMaxWidth(Double.MAX_VALUE);
        messageCellFX.getCancelButton().setMinHeight(50);

        account = LoggedInAccountHolder.getInstance().getAccount();
        // Image border radius van 8px geven om consistent te blijven met de rest
        Rectangle clip = new Rectangle(50, 50);
        clip.setArcWidth(8);
        clip.setArcHeight(8);
        // Maak een ImageView aan met de gebruikers of bot profielafbeelding

        messageCellFX.getImageView().setFitWidth(50);
        messageCellFX.getImageView().setFitHeight(50);
        messageCellFX.getImageView().setClip(clip);

        // Use a GridPane instead of a HBox
        GridPane messageGrid = new GridPane();
        messageGrid.add(messageCellFX.getImageView(), 0, 0);
        messageGrid.add(messageCellFX.getMessageTextArea(), 1, 0);
        messageGrid.add(messageCellFX.getEditButton(), 2, 0);
        messageGrid.add(messageCellFX.getDeleteButton(), 3, 0);
        messageGrid.add(messageCellFX.getCopyButton(), 2, 0);
        messageGrid.add(messageCellFX.getSaveButton(), 2, 0);
        messageGrid.add(messageCellFX.getCancelButton(), 3, 0);
        messageGrid.setHgap(10);
        messageGrid.setAlignment(Pos.CENTER_LEFT);

        // Set the column constraints to fit the content
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.NEVER);
        cc1.setFillWidth(false);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        cc2.setFillWidth(true);
        ColumnConstraints cc3 = new ColumnConstraints();
        cc3.setHgrow(Priority.SOMETIMES);
        cc3.setFillWidth(true);
        ColumnConstraints cc4 = new ColumnConstraints();
        cc4.setHgrow(Priority.SOMETIMES);
        cc4.setFillWidth(true);
        messageGrid.getColumnConstraints().addAll(cc1, cc2, cc3, cc4);

        updateCell(message, messageCellFX);

        // Return a new cell with the message grid as its node
        return new Cell<>() {
            @Override
            public Node getNode() {
                return messageGrid;
            }

            @Override
            public void updateItem(MessageInterface message) {
                updateCell(message, messageCellFX);
            }
        };
    }

    // Method to update the cell based on the message user type
    private void updateCell(MessageInterface message, MessageCellFX messageCellFX) {

        // Set the text of the message text area
        messageCellFX.getMessageTextArea().setText(message.getText());

        // Set the style of the buttons
        // due to MFXManager buttons can't get styling from CSS files unless an id is used, however if we disable it the commented code should work
        //messageCellFX.getCopyButton().getStyleClass().add("message-cell-copy-button");
        //messageCellFX.getSaveButton().getStyleClass().add("message-cell-save-button");
        //messageCellFX.getDeleteButton().getStyleClass().add("message-cell-delete-button");
        //messageCellFX.getEditButton().getStyleClass().add("message-cell-edit-button");
        //messageCellFX.getCancelButton().getStyleClass().add("message-cell-cancel-button");


        if (account.getSettings().getTheme() == ThemeType.LIGHT) {
            messageCellFX.getCopyButton().setStyle(CSSStyling.MESSAGE_CELL_COPY_BUTTON_LIGHT);
            messageCellFX.getSaveButton().setStyle(CSSStyling.MESSAGE_CELL_SAVE_BUTTON_LIGHT);
            messageCellFX.getDeleteButton().setStyle(CSSStyling.MESSAGE_CELL_DELETE_BUTTON_LIGHT);
            messageCellFX.getEditButton().setStyle(CSSStyling.MESSAGE_CELL_EDIT_BUTTON_LIGHT);
            messageCellFX.getCancelButton().setStyle(CSSStyling.MESSAGE_CELL_CANCEL_BUTTON_LIGHT);
        }
        if (account.getSettings().getTheme() == ThemeType.DARK) {
            messageCellFX.getCopyButton().setStyle(CSSStyling.MESSAGE_CELL_COPY_BUTTON_DARK);
            messageCellFX.getSaveButton().setStyle(CSSStyling.MESSAGE_CELL_SAVE_BUTTON_DARK);
            messageCellFX.getDeleteButton().setStyle(CSSStyling.MESSAGE_CELL_DELETE_BUTTON_DARK);
            messageCellFX.getEditButton().setStyle(CSSStyling.MESSAGE_CELL_EDIT_BUTTON_DARK);
            messageCellFX.getCancelButton().setStyle(CSSStyling.MESSAGE_CELL_CANCEL_BUTTON_DARK);
        }

        MessageCellUpdateInterface messageCellUpdater;
        if (message.isUser()) {
            messageCellUpdater = new MessageCellUpdateUser(account);
            messageCellUpdater.updateMessageCell(messageCellFX, message);
        } else {
            messageCellUpdater = new MessageCellUpdateBot();
            messageCellUpdater.updateMessageCell(messageCellFX, message);
        }
    }
}