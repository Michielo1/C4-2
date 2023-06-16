package hhs4a.project2.c42.enums;

public enum ThemeType {

    LIGHT("Light"),
    DARK("Dark");

    private String type;

    ThemeType(String type) { this.type = type; }

    public String getType() { return this.type; }

}
