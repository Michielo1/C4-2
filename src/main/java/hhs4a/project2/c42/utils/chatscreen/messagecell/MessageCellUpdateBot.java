package hhs4a.project2.c42.utils.chatscreen.messagecell;

import hhs4a.project2.c42.utils.chatscreen.Question;
import hhs4a.project2.c42.utils.fx.ChangeButtonVisibility;
import hhs4a.project2.c42.utils.message.MessageInterface;
import hhs4a.project2.c42.utils.message.UserMessage;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Objects;

public class MessageCellUpdateBot implements MessageCellUpdateInterface{
    public void updateMessageCell(MessageCellFX messageCellFX, MessageInterface message) {
        //We need to remove the old styling before adding the new one. If we do .clear() it doesnt want to apply the new styling for some reason.
        UserMessage userMessage = new UserMessage(new Question(""));
        messageCellFX.getMessageTextArea().getStyleClass().removeAll(userMessage.getSenderColor());
        messageCellFX.getMessageTextArea().getStyleClass().add(message.getSenderColor());


        messageCellFX.getImageView().setImage(new Image(Objects.requireNonNull(getClass().getResource("/hhs4a/project2/c42/images/c4-2_bot.png")).toExternalForm()));

        // Knoppen configureren
        ChangeButtonVisibility.hideButton(new MFXButton[]{ messageCellFX.getSaveButton(), messageCellFX.getCancelButton(), messageCellFX.getEditButton(), messageCellFX.getDeleteButton()});
        ChangeButtonVisibility.showButton(new MFXButton[]{messageCellFX.getCopyButton()});

        // Actie voor het kopieer knop
        messageCellFX.getCopyButton().setOnAction(event -> {
            String textToCopy = message.getText();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(textToCopy);
            clipboard.setContent(content);
        });
    }
}
