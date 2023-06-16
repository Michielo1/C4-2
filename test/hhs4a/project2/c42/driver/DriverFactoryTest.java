package hhs4a.project2.c42.driver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DriverFactoryTest {

    private DriverFactory driverFactory;
    @BeforeEach
    void setUp() {
        driverFactory = new DriverFactory();
    }

    @Test
    @DisplayName("Create driver throws error when invalid driver type is given")
    void createDriverThrowErrorWhenInvalid() {
        assertThrows(RuntimeException.class, () -> driverFactory.createDriver("Invalid", true));
    }

    @ParameterizedTest
    @MethodSource("driverTypes")
    void createDriverTest(String type) {
        Driver driver = driverFactory.createDriver(type, true);
        assertNotNull(driver);
        assertEquals(type, driver.getClass().getSimpleName());
    }

    //All currently supported driver types
    static Stream<String> driverTypes() {
        return Stream.of("ChromeDriver", "FirefoxDriver", "EdgeDriver", "OperaDriver");
    }

}