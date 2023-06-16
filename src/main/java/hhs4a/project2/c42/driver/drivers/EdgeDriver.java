package hhs4a.project2.c42.driver.drivers;

import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.driver.Driver;

import com.microsoft.edge.seleniumtools.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class EdgeDriver extends Driver {


    @Override
    public void launch() {
        WebDriverManager.edgedriver().setup();
        if (super.launchAsHeadless) {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--headless");
            super.driver = new org.openqa.selenium.edge.EdgeDriver(edgeOptions);
        } else {
            super.driver = new org.openqa.selenium.edge.EdgeDriver();
        }
    }

    @Override
    public boolean isCompatible() {
        WebDriverManager.edgedriver().setup();
        return (!WebDriverManager.edgedriver().getBrowserPath().toString().equals("Optional.empty"));
    }

    @Override
    public void download() {
        WebDriverManager.edgedriver().forceDownload();
        WebDriverManager.edgedriver().setup();
        if (!WebDriverManager.edgedriver().getBrowserPath().toString().equals("Optional.empty")) {
            System.out.println("Download successful!");
        } else {
            System.out.println("Download failed");
        }
    }

    @Override
    public DriverEnum getDriverType() {
        return DriverEnum.EDGE;
    }
}
