package hhs4a.project2.c42.driver.drivers;

import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.driver.Driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriver extends Driver {

    @Override
    public void launch() {
        WebDriverManager.firefoxdriver().setup();
        if (super.launchAsHeadless) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--headless");
            super.driver = new org.openqa.selenium.firefox.FirefoxDriver(firefoxOptions);
        } else {
            super.driver = new org.openqa.selenium.firefox.FirefoxDriver();
        }
    }

    @Override
    public boolean isCompatible() {
        WebDriverManager.firefoxdriver().setup();
        return (!WebDriverManager.firefoxdriver().getBrowserPath().toString().equals("Optional.empty"));
    }

    @Override
    public void download() {
        WebDriverManager.firefoxdriver().forceDownload();
        WebDriverManager.firefoxdriver().setup();
        if (!WebDriverManager.firefoxdriver().getBrowserPath().toString().equals("Optional.empty")) {
            System.out.println("Download successful!");
        } else {
            System.out.println("Download failed");
        }
    }

    @Override
    public DriverEnum getDriverType() {
        return DriverEnum.FIREFOX;
    }

}
