package hhs4a.project2.c42.utils.account;

public class LoggedInAccountHolder {
    private Account account;

    // volatile keyword ensures that multiple threads handle the instance correctly
    // If one thread tries to access the instance while another thread is creating it, the first thread would read an incorrect value for instance. Volatile fixes this.
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile LoggedInAccountHolder instance;

    private LoggedInAccountHolder() {
        this.account = new Account();
    }

    // Singleton pattern
    //Syncronized keyword ensures that only one thread can access the method at a time
    // Use volatile to ensure visibility of the instance across threads.
    public static LoggedInAccountHolder getInstance() {
        if(instance == null) {
            synchronized (LoggedInAccountHolder.class) {
                if (instance == null) {
                    instance = new LoggedInAccountHolder();
                }
            }
        }
        return instance;
    }

    public void setAccount(){
        account = new Account();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }


}
