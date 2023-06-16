package hhs4a.project2.c42.utils.email;

public class TestEmailUtil {

    // Geen database, dus voor nu even zo testen
    public static String[] emailsInUse = {"your@email.com", "mylittle@pony.org", "iamthepresident@usa.org", "suntsu@gmail.com"};

    public static boolean testEmailInUse(String email) {

        for (String usedEmail : emailsInUse) {
            if (usedEmail.equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }

    public static boolean testEmailValidCheck(String emaiil) {
        return !testEmailInUse(emaiil) && EmailUtil.validateEmail(emaiil);
    }
}