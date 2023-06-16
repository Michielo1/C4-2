package hhs4a.project2.c42.chatmodel;

import hhs4a.project2.c42.enums.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiConnectionTest {
    @Test
    void validModelTest(){
        // Arrange
        AiConnection aiConnection = new AiConnection(Model.AIML_NL);

        // Act
        boolean result = aiConnection.hasValidModel();

        // Assert
        assertTrue(result);
    }

    @Test
    void notValidModelTest(){
        // Arrange
        AiConnection aiConnection = new AiConnection(null);

        // Act
        boolean result = aiConnection.hasValidModel();

        // Assert
        assertFalse(result);
    }

}