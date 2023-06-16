package hhs4a.project2.c42.utils.chatscreen;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.utils.message.MessageInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConversationHistory {
    private List<MessageInterface> conversation;      // the list of messages in the conversation
    private Date date;                      // the date the conversation was last modified
    private Chat chat;                      // the latest chat object that this conversation belongs to
    private int id;                         // the id of the conversationHistory


    // this constructor is used for when the chat doesn't have an ID linked to it
    //TODO generateChatId() kan hiervoor gebruikt worden
    //TODO implement dutch/but this needs to be saved in the database

    // this constructor can be used for retrieving a chat history
    public ConversationHistory(Chat chat, List<MessageInterface> conversation, Date date, int id) {
        this.conversation = conversation;
        this.date = date;
        this.chat = chat;
        this.id = id;
    }

    public Model getModel() {
        return chat.getModel();
    }

    public void setConversation(List<MessageInterface> conversation) {
        this.conversation = conversation;
        this.date = new Date(System.currentTimeMillis());   // update the date whenever the conversation is modified
    }

    public List<MessageInterface> getConversation() {
        return conversation;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  // create a date format object
        return dateFormat.format(date);   // format the date as a string
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // this method is used to update the conversation History
    public void setConversationHistory(ConversationHistory conversationHistory) {
        this.conversation = conversationHistory.getConversation();
        this.date = conversationHistory.getDate();
        this.chat = conversationHistory.getChat();
        this.id = conversationHistory.getId();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}