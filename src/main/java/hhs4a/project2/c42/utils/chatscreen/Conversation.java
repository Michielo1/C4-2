package hhs4a.project2.c42.utils.chatscreen;

public class Conversation {
    private Question question;
    private Answer answer;
    private Chat chat;

    private int id;

    public Conversation(Question question, Answer answer, Chat chat, int id) {
        this.question = question;
        this.answer = answer;
        this.chat = chat;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
