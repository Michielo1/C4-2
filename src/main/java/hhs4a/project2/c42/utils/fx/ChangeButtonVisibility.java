package hhs4a.project2.c42.utils.fx;

import io.github.palexdev.materialfx.controls.MFXButton;

public class ChangeButtonVisibility {
    //Looping through the buttons and setting them to visible and enabled
    public static void showButton(MFXButton[] mfxButtons) {
        for (MFXButton mfxButton : mfxButtons) {
            mfxButton.setVisible(true);
            mfxButton.setDisable(false);
        }
    }

    //Looping through the buttons and setting them to invisible and disabled
    public static void hideButton(MFXButton[] mfxButtons) {
        for (MFXButton mfxButton : mfxButtons) {
            mfxButton.setVisible(false);
            mfxButton.setDisable(true);
        }
    }
}
