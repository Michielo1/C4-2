package hhs4a.project2.c42.driver.drivers;

import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.driver.Driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.opera.OperaOptions;

public class OperaDriver extends Driver {

    @Override
    public void launch() {
        WebDriverManager.operadriver().setup();
        if (super.launchAsHeadless) {
            OperaOptions operaOptions = new OperaOptions();
            operaOptions.addArguments("--headless");
            super.driver = new org.openqa.selenium.opera.OperaDriver(operaOptions);
        } else {
            super.driver = new org.openqa.selenium.opera.OperaDriver();
        }
    }

    @Override
    public boolean isCompatible() {
        WebDriverManager.operadriver().setup();
        return (!WebDriverManager.operadriver().getBrowserPath().toString().equals("Optional.empty"));
    }

    @Override
    public void download() {
        WebDriverManager.operadriver().forceDownload();
        WebDriverManager.operadriver().setup();
        if (!WebDriverManager.operadriver().getBrowserPath().toString().equals("Optional.empty")) {
            System.out.println("Download successful!");
        } else {
            System.out.println("Download failed");
        }
    }

    @Override
    public DriverEnum getDriverType() {
        return DriverEnum.OPERA;
    }
}
