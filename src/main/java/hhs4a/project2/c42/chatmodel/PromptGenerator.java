package hhs4a.project2.c42.chatmodel;

import hhs4a.project2.c42.driver.Driver;

public interface PromptGenerator {

    boolean isRunning();
    String generatePrompt(PromptConfiguration config);
    String generatePrompt(PromptConfiguration config, Driver driver);
    String getModelName();
    void cancelPrompt();

}
