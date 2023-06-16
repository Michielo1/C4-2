package hhs4a.project2.c42.utils.account;

import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;

import java.time.LocalDate;
import java.util.Random;

public class AccountGenerator {

    public static String generateEmail(boolean valid){
        String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String NOT_ALLOWED_CHARACTERS_WITH_SPECIAL_CHARACTERS = "!@#$%^&*()_+";

        String characters = valid ? ALLOWED_CHARACTERS : NOT_ALLOWED_CHARACTERS_WITH_SPECIAL_CHARACTERS;

        StringBuilder randomBeforeAt = new StringBuilder();
        StringBuilder randomAfterAt = new StringBuilder();

        Random random = new Random();
        int length = random.nextInt(10 - 3 + 1) + 3;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomBeforeAt.append(randomChar);
        }

        length = random.nextInt(5 - 2 + 1) + 2;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomAfterAt.append(randomChar);
        }

        return randomBeforeAt + "@" + randomAfterAt + ".com";
    }

    //Generating random passwords for testing.
    public static String generatePassword(boolean[] conditions) {
        // Create a StringBuilder object to store the password
        StringBuilder password = new StringBuilder();
        // Define the possible characters for each condition
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digit = "0123456789";
        String special = "!@#$%^&*=_<>?.,;:|`~";
        // Loop through the boolean array and append a random character that satisfies the corresponding condition
        for (int i = 0; i < conditions.length; i++) {
            if (conditions[i]) {
                // Choose a random character from the appropriate string
                switch (i) {
                    case 0 -> password.append(uppercase.charAt((int) (Math.random() * uppercase.length())));
                    case 1 -> password.append(lowercase.charAt((int) (Math.random() * lowercase.length())));
                    case 2 -> password.append(digit.charAt((int) (Math.random() * digit.length())));
                    case 3 -> password.append(special.charAt((int) (Math.random() * special.length())));
                }
            } else {
                // Choose a random character from any of the other strings
                String other = switch (i) {
                    case 0 -> lowercase + digit + special;
                    case 1 -> uppercase + digit + special;
                    case 2 -> uppercase + lowercase + special;
                    case 3 -> uppercase + lowercase + digit;
                    default -> "";
                };
                password.append(other.charAt((int) (Math.random() * other.length())));
            }
        }
        // Return the password as a string
        return password.toString();
    }

    public static Account generateAccount(){
        Account account = new Account();
        String email = generateEmail(true);
        account.setEmail(email);
        account.setPassword(generatePassword(new boolean[]{true, true, true, true}));
        account.setUsername(email.split("@")[0]);
        account.setId(AccountDatabaseUtil.getInstance().processAddObject(account));
        return account;
    }

    public static String randomDate(){
        String lastActive = "2021-06-01";
        LocalDate lastActiveDate = LocalDate.parse(lastActive);

        LocalDate currentDate = LocalDate.now();
        Random random = new Random();

        LocalDate randomDate = generateRandomDate(lastActiveDate, currentDate, random);
        System.out.println(randomDate);
        return randomDate.toString();
    }

    private static LocalDate generateRandomDate(LocalDate startDate, LocalDate endDate, Random random) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();

        long randomEpochDay = startEpochDay + random.nextInt((int) (endEpochDay - startEpochDay));
        return LocalDate.ofEpochDay(randomEpochDay);
    }
}
