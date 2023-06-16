package hhs4a.project2.c42.chatmodel;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.chatmodel.aiml.AimlPromptConfiguration;
import hhs4a.project2.c42.chatmodel.aiml.AimlPromptGeneratorENG;
import hhs4a.project2.c42.chatmodel.aiml.AimlPromptGeneratorNL;
import hhs4a.project2.c42.chatmodel.alpaca.AlpacaPromptConfiguration;
import hhs4a.project2.c42.chatmodel.alpaca.AlpacaPromptGenerator;
import hhs4a.project2.c42.utils.aiml.AimlBot;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;

public class AiConnection {

    private Model model;
    private boolean hasValidModel;
    private PromptGenerator gen;

    public AiConnection(Model model) {

        boolean isValidModel = false;
        PromptGenerator[] promptGenerators = {new AlpacaPromptGenerator(), new AimlPromptGeneratorENG(), new AimlPromptGeneratorNL()};
        for (PromptGenerator promptGenerator : promptGenerators) {
            System.out.println(promptGenerator.getModelName() + " " + model);
            if (promptGenerator.getModelName().equalsIgnoreCase(String.valueOf(model))) {
                isValidModel = true;
                break;
            }
        }

        if (!isValidModel) {
            System.out.println("INVALID MODEL");
            return;
        }

        this.model = model;
        this.hasValidModel = true;
    }

    public String prompt(ConversationHistory conversationHistory) {
        if (model == Model.ALPACA13B) {
            PromptConfiguration config = new AlpacaPromptConfiguration(conversationHistory, 2000, true, 7);
            gen = new AlpacaPromptGenerator();
            return gen.generatePrompt(config);
        } else if (model == Model.AIML_ENG) {
            AimlBot.getInstance().initializeAimlBot(conversationHistory);
            PromptConfiguration config = new AimlPromptConfiguration(conversationHistory, 2000, true, 4);
            PromptGenerator gen = new AimlPromptGeneratorENG();
            return gen.generatePrompt(config);
        } else if (model == Model.AIML_NL) {
            AimlBot.getInstance().initializeAimlBot(conversationHistory);
            PromptConfiguration config = new AimlPromptConfiguration(conversationHistory, 2000, true, 4);
            PromptGenerator gen = new AimlPromptGeneratorNL();
            return gen.generatePrompt(config);
        }
        return null;
    }

    public void cancelPrompt() {
        if(gen != null) {
            gen.cancelPrompt();
        }
    }

    public boolean hasValidModel() {
        return hasValidModel;
    }
}
