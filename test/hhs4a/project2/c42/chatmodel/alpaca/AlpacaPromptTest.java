package hhs4a.project2.c42.chatmodel.alpaca;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.utils.chatscreen.Answer;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.chatscreen.Question;
import hhs4a.project2.c42.utils.message.BotMessage;
import hhs4a.project2.c42.utils.message.MessageInterface;
import hhs4a.project2.c42.utils.message.UserMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AlpacaPromptTest {

    // NOTE: This test can be expected to take up to 3 minutes for each method!
    // The heavier chat models WILL take a long time to complete, this is normal!

    /*
        Set up console outputStream to be able to read the output
     */
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeAll
    public static void setup() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(System.out);
    }

    /*
        Simulate a single prompt message
     */

    @Test
    public void alpacaSingleTest() {

        // simulating message
        List<MessageInterface> messages = new ArrayList<>();
        Question question = new Question("How are you?",  -1);
        messages.add(new UserMessage(question));

        // creating conversationHistory object with the simulated message
        Chat chat = new Chat(-1, "Test", Model.ALPACA13B, String.valueOf( new Date(System.currentTimeMillis())),  "english");
        ConversationHistory conversationHistory = new ConversationHistory(chat, messages, new Date(System.currentTimeMillis()),  -1);

        // create config object
        AlpacaPromptConfiguration config = new AlpacaPromptConfiguration(conversationHistory, 2000, true, 4);
        // create gen object
        AlpacaPromptGenerator generator = new AlpacaPromptGenerator();
        // run prompt and save response
        String response = generator.generatePrompt(config);

        // assuming it works, if it doesn't we skip the test
        boolean isAvailable = !outContent.toString().contains("Bot not available!");
        Assumptions.assumeTrue(isAvailable);

        // asserting conditions
        assertNotNull(response);
        org.junit.jupiter.api.Assertions.assertFalse(response.isBlank());
        boolean hasError = outContent.toString().contains("ERROR! Could not generate an output. Please try again later!");
        org.junit.jupiter.api.Assertions.assertFalse(hasError);
    }

    @Test
    public void alpacaMultipleTest() {

        // simulating message
        List<MessageInterface> messages = new ArrayList<>();

        Question question1 = new Question("How are you?",  -1);
        Question question2 = new Question("What are you?",  -1);
        Answer answer1 = new Answer("I'm good, thanks! What can I help you with?", -1);

        messages.add(new UserMessage(question1));
        messages.add(new BotMessage(answer1));
        messages.add(new UserMessage(question2));

        // creating conversationHistory object with the simulated message
        Chat chat = new Chat(-1, "Test", Model.ALPACA13B, String.valueOf( new Date(System.currentTimeMillis())),  "english");
        ConversationHistory conversationHistory = new ConversationHistory(chat, messages, new Date(System.currentTimeMillis()),  -1);

        // create config object
        AlpacaPromptConfiguration config = new AlpacaPromptConfiguration(conversationHistory, 2000, true, 4);
        // create gen object
        AlpacaPromptGenerator generator = new AlpacaPromptGenerator();
        // run prompt and save response
        String response = generator.generatePrompt(config);

        // assuming it works, if it doesn't we skip the test
        boolean isAvailable = !outContent.toString().contains("Bot not available!");
        Assumptions.assumeTrue(isAvailable);

        // asserting conditions
        assertNotNull(response);
        org.junit.jupiter.api.Assertions.assertFalse(response.isBlank());
        boolean hasError = outContent.toString().contains("ERROR! Could not generate an output. Please try again later!");
        org.junit.jupiter.api.Assertions.assertFalse(hasError);
    }

    @Test
    void alpacaCancelTest(){
        // simulating message
        List<MessageInterface> messages = new ArrayList<>();
        // long prompt to stall alpacca
        Question question = new Question("Hello good sir. How are you? And could you tell me the time?",  -1);
        messages.add(new UserMessage(question));

        // creating conversationHistory object with the simulated message
        Chat chat = new Chat(-1, "Nederlands Test", Model.ALPACA13B, String.valueOf( new Date(System.currentTimeMillis())),  "english");
        ConversationHistory conversationHistory = new ConversationHistory(chat, messages, new Date(System.currentTimeMillis()),  -1);

        // create config object
        AlpacaPromptConfiguration config = new AlpacaPromptConfiguration(conversationHistory, 2000, true, 4);

        // create gen object
        AlpacaPromptGenerator generator = new AlpacaPromptGenerator();
        // run prompt and save response
        AtomicReference<String> response = new AtomicReference<>();
        Thread thread = new Thread(() -> response.set(generator.generatePrompt(config)));
        thread.start();

        // waiting 4 seconds before cancelling the prompt because selenium takes a bit to load.
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        generator.cancelPrompt();

        // joining thread so the rest of the code only runs after the response is actually cancelled
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // assuming it works, if it doesn't we skip the test
        boolean isAvailable = !outContent.toString().contains("Bot not available!");
        Assumptions.assumeTrue(isAvailable);

        String actual = response.get();

        assertNotNull(actual);
        assertEquals("CANCELLED", actual);
    }

    //todo maybe add a dutch test here, but I dont really know how we could test that -Tjorn

}