package hhs4a.project2.c42.driver;

import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.driver.drivers.ChromeDriver;
import hhs4a.project2.c42.driver.drivers.EdgeDriver;
import hhs4a.project2.c42.driver.drivers.FirefoxDriver;
import hhs4a.project2.c42.driver.drivers.OperaDriver;
import org.openqa.selenium.WebDriver;

public class DriverUtil {

    /*
        Singleton implementation
     */

    private static DriverUtil instance;
    public static DriverUtil getInstance() {
        if (instance == null) instance = new DriverUtil();
        return instance;
    }

    private DriverUtil() { }

    /*
        DriverEnum methods
     */

    private static final Driver[] drivers = {new ChromeDriver(), new EdgeDriver(), new FirefoxDriver(), new OperaDriver()};
    public boolean hasCompatibleMethod() {
        for (Driver driver : drivers) {
            if (driver.isCompatible()) return true;
        }
        return false;
    }

    public Driver useSpecificDriver(DriverEnum driver) {
        System.out.println("Looking for " + driver.toString() + " driver");
        for (Driver d : drivers) {
            if (d.getDriverType() == driver) {
                if(d.isCompatible()) return new DriverFactory().createDriver(d.getClass().getSimpleName(), true);
                else return null;
            }
        }
        return null;
    }

    public Driver getAnyDriver() {
        for (Driver driver : drivers) {
            if (driver.isCompatible()) return new DriverFactory().createDriver(driver.getClass().getSimpleName(), true);
        }
        return null;
    }

    public void downloadAnyDriver() {
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.download();
    }

    public WebDriver prepareAlpacaWebdriver(Driver driver) {
        driver.launch();
        WebDriver webDriver = driver.getDriver();

            /*
                Executing prompt
             */

        System.out.println("Connecting...");

        webDriver.get("<Redacted for archive>");

        // delay clearing input to avoid failing this process as the site may still be loading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return webDriver;
    }

}
