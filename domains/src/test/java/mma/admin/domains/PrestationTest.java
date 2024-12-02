package mma.admin.domains;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Les Tests on étaient développé via l'aide d'une IA
class PrestationTest {

    @Test
    void should_return_id_when_get_id_is_called() {
        // Arrange
        int id = 101;
        String description = "Catering Service";
        Prestation prestation = new Prestation(id, description);

        // Act
        int result = prestation.id();

        // Assert
        assertEquals(id, result);
    }

    @Test
    void should_return_description_when_get_description_is_called() {
        // Arrange
        int id = 101;
        String description = "Catering Service";
        Prestation prestation = new Prestation(id, description);

        // Act
        String result = prestation.description();

        // Assert
        assertEquals(description, result);
    }
}

