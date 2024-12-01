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
        int result = prestation.getId();

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
        String result = prestation.getDescription();

        // Assert
        assertEquals(description, result);
    }

    @Test
    void should_update_description_when_set_description_is_called() {
        // Arrange
        int id = 101;
        String initialDescription = "Catering Service";
        String updatedDescription = "Audio Equipment Rental";
        Prestation prestation = new Prestation(id, initialDescription);

        // Act
        prestation.setDescription(updatedDescription);

        // Assert
        assertEquals(updatedDescription, prestation.getDescription());
    }

    @Test
    void it_should_return_default_values_when_default_constructor_is_used() {
        // Arrange
        Prestation prestation = new Prestation();

        // Act & Assert
        assertEquals(0, prestation.getId());
        assertNull(prestation.getDescription());
    }

    @Test
    void given_parameters_when_constructor_is_called_then_fields_should_be_initialized() {
        // Arrange
        int id = 202;
        String description = "Conference Room Setup";

        // Act
        Prestation prestation = new Prestation(id, description);

        // Assert
        assertEquals(id, prestation.getId());
        assertEquals(description, prestation.getDescription());
    }
}

