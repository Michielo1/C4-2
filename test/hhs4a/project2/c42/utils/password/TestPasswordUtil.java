package hhs4a.project2.c42.utils.password;

//This class is needed because we need to know why the password failed for condition, decision and mcdc testing
public class TestPasswordUtil {
    public static PasswordValidationResult checkIfPasswordValid(String password) {
        if (!PasswordUtil.checkIfPasswordValid(password)) {
            if (!password.matches("^(?=.*[A-Z]).+$")) {
                return PasswordValidationResult.MISSING_UPPERCASE_LETTER;
            }
            if (!password.matches("^(?=.*[a-z]).+$")) {
                return PasswordValidationResult.MISSING_LOWERCASE_LETTER;
            }
            if (!password.matches("^(?=.*\\d).+$")) {
                return PasswordValidationResult.MISSING_DIGIT;
            }
            if (!password.matches("^(?=.*[!@#$%^&*=_<>?.,;:|`~]).+$")) {
                return PasswordValidationResult.MISSING_SPECIAL_CHARACTER;
            }
        }
        return PasswordValidationResult.VALID;
    }
}
