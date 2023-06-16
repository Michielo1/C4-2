package hhs4a.project2.c42.utils.message;

public interface MessageInterface {
    String getText();
    boolean isUser();
    String getSender();
    int getId();
    String getSenderColor();
    void setText(String text);
}
