package hhs4a.project2.c42.utils.chatscreen.messagecell;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class MessageCellFX {
    private final MFXButton editButton = new MFXButton("Bewerken");
    private final MFXButton deleteButton = new MFXButton("Verwijderen");

    private final MFXButton copyButton = new MFXButton("Kopieer");
    private final MFXButton saveButton = new MFXButton("Opslaan");
    private final MFXButton cancelButton = new MFXButton("Annuleren");
    private final TextArea messageTextArea = new TextArea();
    private final ImageView imageView = new ImageView();

    public MFXButton getEditButton() {
        return editButton;
    }

    public MFXButton getDeleteButton() {
        return deleteButton;
    }

    public MFXButton getCopyButton() {
        return copyButton;
    }

    public MFXButton getSaveButton() {
        return saveButton;
    }

    public MFXButton getCancelButton() {
        return cancelButton;
    }

    public TextArea getMessageTextArea() {
        return messageTextArea;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
