package hhs4a.project2.c42.utils.css;

import java.util.Objects;

public class CSSLoader {
    public static String loadCSS(String cssPath) {
        return Objects.requireNonNull(CSSLoader.class.getResource(cssPath)).toExternalForm();
    }
}
