package hhs4a.project2.c42.utils.account;

public class Permission {
    private int id;
    private boolean canChatWithOnlineChatbot;
    private boolean canChatWithOfflineChatbot;
    private boolean isAdmin;

    public Permission(){
        this(0, false, true, false);
    }

    public Permission(int id, boolean canChatWithOnlineChatbot, boolean canChatWithOfflineChatbot, boolean isAdmin) {
        this.id = id;
        this.canChatWithOnlineChatbot = canChatWithOnlineChatbot;
        this.canChatWithOfflineChatbot = canChatWithOfflineChatbot;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getCanChatWithOnlineChatbot() {
        return canChatWithOnlineChatbot;
    }

    public void setCanChatWithOnlineChatbot(boolean canChatWithOnlineChatbot) {
        this.canChatWithOnlineChatbot = canChatWithOnlineChatbot;
    }

    public boolean getCanChatWithOfflineChatbot() {
        return canChatWithOfflineChatbot;
    }

    public void setCanChatWithOfflineChatbot(boolean canChatWithOfflineChatbot) {
        this.canChatWithOfflineChatbot = canChatWithOfflineChatbot;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}