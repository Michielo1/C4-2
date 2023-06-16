package hhs4a.project2.c42.utils.email;

import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import javafx.scene.control.Alert;

public class EmailUtil {
    public static boolean checkIfEmailValid(String email) {
        return validateEmail(email) && !isEmailInUse(email);
    }

    public static boolean isEmailInUse(String email) {
        if (AccountDatabaseUtil.getInstance().checkIfEmailExists(email)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "E-mailadres in gebruik", "Het ingevoerde e-mailadres is al in gebruik door een ander account. Kies alstublieft een ander e-mailadres.");
            return true;
        }
        return false;
    }

    public static boolean validateEmail(String email) {
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Ongeldig e-mailadres", "Het ingevoerde e-mailadres is ongeldig. Vul alstublieft een geldig e-mailadres in.");
            return false;
        }
        return true;
    }
}