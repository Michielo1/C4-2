package hhs4a.project2.c42.utils.aiml;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import org.goldrenard.jb.configuration.BotConfiguration;
import org.goldrenard.jb.core.Bot;

import java.io.File;

public class AimlBot {
    private Model previousBotName;

    private Bot bot;

    private static AimlBot instance;

    private AimlBot(){
        instance = this;
    }

    public static AimlBot getInstance() {
        if (instance == null) instance = new AimlBot();
        return instance;
    }

    public Bot getBot() {
        return bot;
    }

    public void initializeAimlBot(ConversationHistory conversationHistory) {
        System.out.println("Initializing AIML bot with language: " + conversationHistory.getChat().getLanguage());
        try {

            if(previousBotName != null && previousBotName == conversationHistory.getChat().getModel()){
                System.out.println("Bot already initialized with this language");
                return;
            }

            String botsPath = getBotsPath();

            BotConfiguration.BotConfigurationBuilder builder = BotConfiguration
                    .builder()
                    .name(conversationHistory.getChat().getModel().toString())
                    .path(botsPath)
                    .action("chat")
                    .jpTokenize(false)
                    .graphShortCuts(true);

            BotConfiguration configuration = builder.build();

            bot = new Bot(configuration);

            previousBotName = Model.valueOf(bot.getName());

            System.out.println("Bot initialized with language: " + previousBotName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This code is used to update the aiml files, but it is not used in the project
    //We might want to update the aiml files periodically.
    //This code is required to run after updating the aiml files
    public void updateAimlFiles(){
        bot.writeAIMLFiles();
        bot.writeAIMLIFFiles();
    }

    // This method is used to get the path to the bots folder
    private static String getBotsPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath + File.separator +"hhs4a" + File.separator + "project2" + File.separator + "c42";
    }
}
