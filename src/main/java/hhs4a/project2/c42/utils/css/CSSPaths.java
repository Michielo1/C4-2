package hhs4a.project2.c42.utils.css;

import hhs4a.project2.c42.enums.ThemeType;

import java.util.HashMap;
import java.util.Map;

public class CSSPaths {

    private static CSSPaths instance;
    private Map<ThemeType, String> themeCSS;
    public static final String LIGHT_MODE_CSS = "/hhs4a/project2/c42/css/LightModeCSS.css";
    public static final String DARK_MODE_CSS = "/hhs4a/project2/c42/css/DarkModeCSS.css";
    public static final String LOGIN_SCREEN_CSS = "/hhs4a/project2/c42/css/LoginScreenCSS.css";

    public static CSSPaths getInstance() {
        return instance == null ? new CSSPaths() : instance;
    }

    public CSSPaths() {
        themeCSS = new HashMap<>();
        themeCSS.put(ThemeType.LIGHT, LIGHT_MODE_CSS);
        themeCSS.put(ThemeType.DARK, DARK_MODE_CSS);
    }

    public String getThemePath(ThemeType themeType) {
        return themeCSS.get(themeType);
    }
}
