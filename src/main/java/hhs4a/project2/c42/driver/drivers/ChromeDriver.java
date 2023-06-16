package hhs4a.project2.c42.driver.drivers;

import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.driver.Driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriver extends Driver {

    @Override
    public void launch() {
        WebDriverManager.chromedriver().setup();
        if (super.launchAsHeadless) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless");
            super.driver = new org.openqa.selenium.chrome.ChromeDriver(chromeOptions);
        } else {
            super.driver = new org.openqa.selenium.chrome.ChromeDriver();
        }
    }

    @Override
    public boolean isCompatible() {
        WebDriverManager.chromedriver().setup();
        return (!WebDriverManager.chromedriver().getBrowserPath().toString().equals("Optional.empty"));
    }

    @Override
    public void download() {
        WebDriverManager.chromedriver().forceDownload();
        WebDriverManager.chromedriver().setup();
        if (!WebDriverManager.chromedriver().getBrowserPath().toString().equals("Optional.empty")) {
            System.out.println("Download successful!");
        } else {
            System.out.println("Download failed");
        }
    }

    @Override
    public DriverEnum getDriverType() {
        return DriverEnum.CHROME;
    }

}
