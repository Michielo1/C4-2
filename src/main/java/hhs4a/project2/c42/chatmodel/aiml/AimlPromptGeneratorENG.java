package hhs4a.project2.c42.chatmodel.aiml;


import hhs4a.project2.c42.chatmodel.PromptConfiguration;
import hhs4a.project2.c42.chatmodel.PromptGenerator;
import hhs4a.project2.c42.driver.Driver;


public class AimlPromptGeneratorENG extends AimlPromptGeneratorTemplate implements PromptGenerator {

    @Override
    public boolean isRunning() {
        return super.isRunning();
    }

    @Override
    public synchronized String generatePrompt(PromptConfiguration config) {
        return super.generatePrompt(config);
    }

    @Override
    public String generatePrompt(PromptConfiguration config, Driver driver) {
        return null;
    }

    @Override
    public String getModelName() {
        return "AIML_ENG";
    }

    @Override
    public void cancelPrompt() {
        super.cancelPrompt();
    }
}