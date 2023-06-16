package hhs4a.project2.c42.utils.chatscreen;

import hhs4a.project2.c42.enums.Model;

public class Chat {
    private int chatId;
    private String subject;
    private Model model;
    private String lastActive;
    private String language;

    public Chat(int chatId, String subject, Model model, String lastActive, String language) {
        this.chatId = chatId;
        this.subject = subject;
        this.model = model;
        this.lastActive = lastActive;
        this.language = language;
    }

    public Chat(){
        this(-1, "", Model.AIML_NL, "", "Nederlands");
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}