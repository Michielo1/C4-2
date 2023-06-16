package hhs4a.project2.c42.driver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DriverUtilTest {

    private DriverUtil driverUtil;

    @BeforeEach
    void setUp() {
        driverUtil = DriverUtil.getInstance();
    }

    @Test
    @DisplayName("Check if there is a compatible driver")
    void testHasCompatibleMethod() {
        assertTrue(driverUtil.hasCompatibleMethod(), "There should be at least one compatible driver, otherwise the application cannot run");
    }

    @Test
    @DisplayName("Get any compatible driver")
    void testGetAnyDriver() {
        Driver driver = driverUtil.getAnyDriver();
        assertNotNull(driver, "The driver should not be null");
        assertTrue(driver.isCompatible(), "The driver should be compatible with the user's system");
    }

}