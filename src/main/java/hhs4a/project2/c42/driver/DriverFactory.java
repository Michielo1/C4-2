package hhs4a.project2.c42.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DriverFactory {

    public Driver createDriver(String type, boolean headless) {
        try {
            Class<?> clazz = Class.forName("hhs4a.project2.c42.driver.drivers." + type);
            Constructor<?> constructor = clazz.getConstructor();
            Driver instance = (Driver) constructor.newInstance();
            instance.setHeadless(headless);
            return instance;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
