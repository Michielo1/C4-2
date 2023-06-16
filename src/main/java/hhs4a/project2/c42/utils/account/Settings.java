package hhs4a.project2.c42.utils.account;

import hhs4a.project2.c42.enums.ThemeType;
import hhs4a.project2.c42.enums.DriverEnum;

public class Settings {
    private int id;
    private DriverEnum diverOption;
    private ThemeType themeType;
    private String profilePicturePath;

    public Settings(){
        this(0, ThemeType.DARK, "/hhs4a/project2/c42/images/clodsire.jpg" , DriverEnum.CHROME);
    }

    public Settings(int id, ThemeType themeType, String profilePicturePath, DriverEnum driverOption) {
        this.id = id;
        this.themeType = themeType;
        this.diverOption = driverOption;
        this.profilePicturePath = profilePicturePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ThemeType getTheme() {
        return themeType;
    }

    public void setTheme(ThemeType themeType) {
        this.themeType = themeType;
    }

    public void setDiverOption(DriverEnum diverOption) {
        this.diverOption = diverOption;
    }

    public DriverEnum getDiverOption() {
        return diverOption;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}