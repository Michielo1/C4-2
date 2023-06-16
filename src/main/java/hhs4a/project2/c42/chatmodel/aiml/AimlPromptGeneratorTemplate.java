package hhs4a.project2.c42.chatmodel.aiml;

import hhs4a.project2.c42.chatmodel.PromptConfiguration;
import hhs4a.project2.c42.utils.aiml.AimlBot;
import org.goldrenard.jb.configuration.Constants;
import org.goldrenard.jb.core.Bot;
import org.goldrenard.jb.core.Chat;
import org.goldrenard.jb.model.Request;

public class AimlPromptGeneratorTemplate {
    private static final boolean DO_WRITE = false;

    private boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }


    public synchronized String generatePrompt(PromptConfiguration config) {
        Bot bot = AimlBot.getInstance().getBot();

        isRunning = true;

        String prompt = config.getCompletePrompt();

        Chat chatSession = new Chat(bot, DO_WRITE);

        String response;

        if (prompt == null || prompt.length() < 1) {
            prompt = Constants.null_input;
        }

        response = chatSession.multisentenceRespond(Request.builder().input(prompt).build());

        isRunning = false;

        return response;
    }

    void cancelPrompt() {
        isRunning = false;
    }
}
