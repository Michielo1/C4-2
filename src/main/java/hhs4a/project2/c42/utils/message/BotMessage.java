package hhs4a.project2.c42.utils.message;

import hhs4a.project2.c42.utils.chatscreen.Answer;

public class BotMessage implements MessageInterface{
    private final Answer answer; // The answer that was given

    public BotMessage(Answer answer) {
        this.answer = answer;
    }

    @Override
    public String getText() {
        return answer.getAnswer();
    }

    @Override
    public boolean isUser() {
        return false;
    }

    @Override
    public String getSender() {
        return "BOT";
    }

    @Override
    public int getId() {
        return answer.getId();
    }

    @Override
    public String getSenderColor() {
        return "bot-message-cell";
    }

    @Override
    public void setText(String text) {
        answer.setAnswer(text);
    }
}