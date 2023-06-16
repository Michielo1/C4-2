package hhs4a.project2.c42.chatmodel.aiml;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.utils.aiml.AimlBot;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.chatscreen.Question;
import hhs4a.project2.c42.utils.message.MessageInterface;
import hhs4a.project2.c42.utils.message.UserMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AimlPromptTest {
    // Testing the AIML chat model in english
    @Test
    public void aimlEnglish(){
        // simulating message
        List<MessageInterface> messages = new ArrayList<>();

        Question question1 = new Question("What is 1 + 1 ?",  -1);

        messages.add(new UserMessage(question1));
        Chat chat = new Chat(-1, "Test", Model.AIML_ENG, String.valueOf( new Date(System.currentTimeMillis())),  "english");
        ConversationHistory conversationHistory = new ConversationHistory(chat, messages, new Date(System.currentTimeMillis()), -1);

        //Initializing the AIML chat model
        AimlBot.getInstance().initializeAimlBot(conversationHistory);

        // create config object
        AimlPromptConfiguration config = new AimlPromptConfiguration(conversationHistory, 2000, true, 4);
        // create gen object
        AimlPromptGeneratorENG generator = new AimlPromptGeneratorENG();
        // run prompt and save response
        String response = generator.generatePrompt(config);
        String expected = "2.";

        // asserting conditions
        org.junit.jupiter.api.Assertions.assertNotNull(response);
        org.junit.jupiter.api.Assertions.assertFalse(response.isBlank());
        org.junit.jupiter.api.Assertions.assertEquals(expected, response);
    }

    //Using toLowerCase because aiml gets the date from the system. We dont know the system language, so we cant know the language of the date.
    //So we just get the date from the system and then put the entire sting in lowercase, because getting it this way will return the date in caps.
    @Test
    public void aimlDutch(){
        // simulating message
        List<MessageInterface> messages = new ArrayList<>();
        Question question1 = new Question("bedrijf 42",  -1);
        messages.add(new UserMessage(question1));


        Chat chat = new Chat(-1, "Test", Model.AIML_NL, String.valueOf( new Date(System.currentTimeMillis())),  "nederlands");
        ConversationHistory conversationHistory = new ConversationHistory(chat, messages, new Date(System.currentTimeMillis()),  -1);

        //Initializing the AIML chat model
        AimlBot.getInstance().initializeAimlBot(conversationHistory);

        // create config object
        AimlPromptConfiguration config = new AimlPromptConfiguration(conversationHistory, 2000, true, 4);
        // create gen object
        AimlPromptGeneratorNL generator = new AimlPromptGeneratorNL();

        // run prompt and save response
        String response = generator.generatePrompt(config);

        String expected = "Bedrijf 42 is een bedrijf dat java applicaties ontwikkeld";

        // asserting conditions
        org.junit.jupiter.api.Assertions.assertNotNull(response);
        org.junit.jupiter.api.Assertions.assertFalse(response.isBlank());
        org.junit.jupiter.api.Assertions.assertEquals(expected, response);
    }

    @Test
    @DisplayName("Testing the cancel function of the AIML chat model")
    void aimlCancelTest(){
        // simulating message
        List<MessageInterface> messages = new ArrayList<>();
        Question question1 = new Question("what is 1 + 1",  -1);
        messages.add(new UserMessage(question1));

        Chat chat = new Chat(-1, "Test", Model.AIML_ENG, String.valueOf( new Date(System.currentTimeMillis())),  "english");
        ConversationHistory conversationHistory = new ConversationHistory(chat, messages, new Date(System.currentTimeMillis()),  -1);

        //Initializing the AIML chat model
        AimlBot.getInstance().initializeAimlBot(conversationHistory);

        // create config object
        AimlPromptConfiguration config = new AimlPromptConfiguration(conversationHistory, 2000, true, 4);
        // create gen object
        AimlPromptGeneratorENG generator = new AimlPromptGeneratorENG();

        // run prompt and save response
        AtomicReference<String> response = new AtomicReference<>();
        Thread thread = new Thread(() -> response.set(generator.generatePrompt(config)));
        // pausing thread
        thread.interrupt();
        // canceling prompt
        generator.cancelPrompt();
        // resuming thread
        thread.start();

        // joining the thread so the test waits for the thread to finish
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String expected = "2.";
        String actual = response.get();

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isBlank());
        Assertions.assertEquals(expected, actual);
    }
}
