package hhs4a.project2.c42.utils.message;

import hhs4a.project2.c42.utils.chatscreen.Question;

public class UserMessage implements MessageInterface{
    private final Question question; // The question that was asked

    public UserMessage(Question question) {
        this.question = question;
    }

    @Override
    public String getText() {
        return question.getQuestion();
    }

    @Override
    public boolean isUser() {
        return true;
    }

    @Override
    public String getSender() {
        return "USER";
    }

    @Override
    public int getId() {
        return question.getId();
    }

    @Override
    public String getSenderColor() {
        return "user-message-cell";
    }

    @Override
    public void setText(String text) {
        question.setQuestion(text);
    }
}