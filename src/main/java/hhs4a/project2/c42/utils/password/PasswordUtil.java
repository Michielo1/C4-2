package hhs4a.project2.c42.utils.password;

import hhs4a.project2.c42.utils.alert.AlertUtil;
import javafx.scene.control.Alert;

public class PasswordUtil {

    public static boolean verifyPassword(String storedPassword, String password) {
        if (!storedPassword.equals(password)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Foutmelding wachtwoord", "Het ingevoerde wachtwoord is onjuist. Controleer alstublieft uw wachtwoord.");
            return false;
        }
        return true;
    }

    public static boolean matchPassword(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Herhaald wachtwoord onjuist", "Het herhaalde wachtwoord komt niet overeen met het oorspronkelijke wachtwoord.");
            return false;
        }
        return true;
    }

    //Easier to reed like this instead of nested if statements imo -Tjorn
    // split into 4 methods - J.Y. Xie
    public static boolean checkIfPasswordValid(String password) {
        return hasUppercaseLetter(password) && hasLowercaseLetter(password) && hasDigit(password) && hasSpecialCharacter(password);
    }

    private static boolean hasUppercaseLetter(String password) {
        if (!password.matches("^(?=.*[A-Z]).+$")) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Ongeldig Wachtwoord", "Het wachtwoord moet minimaal één hoofdletter bevatten.");
            return false;
        }
        return true;
    }

    private static boolean hasLowercaseLetter(String password) {
        if (!password.matches("^(?=.*[a-z]).+$")) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Ongeldig Wachtwoord", "Het wachtwoord moet minimaal één kleine letter bevatten.");
            return false;
        }
        return true;
    }

    private static boolean hasDigit(String password) {
        if (!password.matches("^(?=.*\\d).+$")) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION,"Ongeldig Wachtwoord", "Het wachtwoord moet minimaal één cijfer bevatten.");
            return false;
        }
        return true;
    }

    private static boolean hasSpecialCharacter(String password) {
        if (!password.matches("^(?=.*[!@#$%^&*=_<>?.,;:|`~]).+$")) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION,"Ongeldig Wachtwoord", "Het wachtwoord moet minimaal één speciaal teken bevatten.");
            return false;
        }
        return true;
    }
}