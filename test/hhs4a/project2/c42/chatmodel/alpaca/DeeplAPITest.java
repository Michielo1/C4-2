package hhs4a.project2.c42.chatmodel.alpaca;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeeplAPITest {
    @Test
    void dutchToEnglishTest(){
        String dutch = "Hallo, hoe gaat het?";
        String english = "Hello, how are you?";

        String translated = DeeplAPI.translate(dutch, "EN");

        assertEquals(english, translated);
    }

    @Test
    void englishToDutchTest(){
        String dutch = "Hallo, hoe gaat het?";
        String english = "Hello, how are you?";

        String translated = DeeplAPI.translate(english, "NL");

        assertEquals(dutch, translated);
    }

}