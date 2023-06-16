package hhs4a.project2.c42.utils.chatscreen;

public class Answer {
    private String answer;
    private int id;

    public Answer(String answer) {
        this(answer, -1);
    }

    public Answer(String answer, int id) {
        this.answer = answer;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
