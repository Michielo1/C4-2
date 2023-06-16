package hhs4a.project2.c42.utils.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmojiFilterTest {

    @Test
    void testRemoveEmojisWithValidText() {
        // Arrange
        String text = "Hello World!";

        // Act
        String result = EmojiFilter.removeEmojis(text);

        // Assert
        assertEquals(text, result);
    }

    @Test
    void testRemoveEmojisWithEmoji() {
        // Arrange
        String text = "Hello World!😀";

        // Act
        String result = EmojiFilter.removeEmojis(text);

        // Assert
        assertEquals("Hello World!", result);
    }

}