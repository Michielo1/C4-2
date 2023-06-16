package hhs4a.project2.c42.driver;

import hhs4a.project2.c42.enums.DriverEnum;
import org.openqa.selenium.WebDriver;

public abstract class Driver {

    protected WebDriver driver;
    private boolean isDestroyed = false;
    protected boolean launchAsHeadless;



    public abstract void launch();

    public abstract boolean isCompatible();

    public abstract void download();

    public void setHeadless(boolean headless) {
        launchAsHeadless = headless;
    }

    public void destroy() {
        isDestroyed = true;
        driver.close();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public abstract DriverEnum getDriverType();

}
