package hhs4a.project2.c42.utils.account;

public class Account {
    private int id; // Id van een account
    private Settings settings; // Setting die is gekoppeld aan een account
    private Permission permissions; // Rechten die zijn gekoppeld aan een account
    private String username; // Gebruikersnaam van een gebruiker
    private String email; // E-mailadres van een gebruiker
    private String password; // Wachtwoord van een gebruiker
    private boolean status; // Status van een gebruiker. True: banned - False: not banned

    public Account(){
        this(0, new Settings(), new Permission(), "", "", "", true);
    }

    public Account(int id, Settings settings, Permission permission, String username, String email, String password, boolean status) {
        this.id = id;
        this.settings = settings;
        this.permissions = permission;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Permission getPermissions() {
        return permissions;
    }

    public void setPermissions(Permission permissions) {
        this.permissions = permissions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}