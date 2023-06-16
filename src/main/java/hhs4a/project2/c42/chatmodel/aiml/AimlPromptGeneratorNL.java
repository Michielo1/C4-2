package hhs4a.project2.c42.chatmodel.aiml;


import hhs4a.project2.c42.chatmodel.PromptConfiguration;
import hhs4a.project2.c42.chatmodel.PromptGenerator;
import hhs4a.project2.c42.driver.Driver;


public class AimlPromptGeneratorNL extends AimlPromptGeneratorTemplate implements PromptGenerator {

    @Override
    public boolean isRunning() {
        return super.isRunning();
    }

    @Override
    public synchronized String generatePrompt(PromptConfiguration config) {
        String prompt = super.generatePrompt(config);
        if(prompt.equals("I have no answer for that.")){
            return "Ik kan hier geen antwoord op geven.";
        }
        return prompt;
    }

    @Override
    public String generatePrompt(PromptConfiguration config, Driver driver) {
        return null;
    }

    @Override
    public String getModelName() {
        return "AIML_NL";
    }

    @Override
    public void cancelPrompt() {
        super.cancelPrompt();
    }
}