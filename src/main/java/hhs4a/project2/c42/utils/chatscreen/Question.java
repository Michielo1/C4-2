package hhs4a.project2.c42.utils.chatscreen;

public class Question {
    private String question;
    private int id;

    public Question(String question) {
        this(question, -1);
    }

    public Question(String question, int id) {
        this.question = question;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
