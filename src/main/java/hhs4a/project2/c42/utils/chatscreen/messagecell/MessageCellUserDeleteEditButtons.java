package hhs4a.project2.c42.utils.chatscreen.messagecell;

import io.github.palexdev.materialfx.controls.MFXButton;

import java.util.ArrayList;

public class MessageCellUserDeleteEditButtons {

    private static final ArrayList<MFXButton> deleteButtons = new ArrayList<>();
    private static final ArrayList<MFXButton> editButtons = new ArrayList<>();

    public static ArrayList<MFXButton> getDeleteButtons(){
        return deleteButtons;
    }

    public static ArrayList<MFXButton> getEditButtons(){
        return editButtons;
    }

    public static void disableDeleteButtons() {
        for (MFXButton deleteButton : deleteButtons) {
            deleteButton.setDisable(true);
        }
    }

    public static void enableDeleteButtons() {
        for (MFXButton deleteButton : deleteButtons) {
            deleteButton.setDisable(false);
        }
    }

    public static void disableEditButtons() {
        for (MFXButton editButton : editButtons) {
            editButton.setDisable(true);
        }
    }

    public static void enableLastEditButton() {
        disableEditButtons();
        editButtons.get(editButtons.size() - 1).setDisable(false);
    }

    public static void clearDeleteAndEditButtons(){
        deleteButtons.clear();
        editButtons.clear();
    }

    public static void addDeleteButton(MFXButton deleteButton){
        if(deleteButtons.contains(deleteButton)){
            return;
        }
        deleteButtons.add(deleteButton);
    }

    public static void addEditButton(MFXButton editButton){
        if(editButtons.contains(editButton)){
            return;
        }
        editButtons.add(editButton);
    }
}
