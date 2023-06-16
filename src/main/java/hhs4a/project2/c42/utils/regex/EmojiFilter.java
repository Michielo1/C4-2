package hhs4a.project2.c42.utils.regex;

public class EmojiFilter {

    public static String removeEmojis(String text) {
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        return text.replaceAll(regex, "");
    }

}
