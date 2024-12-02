package mma.admin.domains;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void should_throw_exception_when_room_id_is_null() {
        // Arrange
        String name = "Conference Room";
        int capacity = 20;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(null, name, capacity));
        assertEquals("L'id de la salle ne peut pas être null.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_capacity_is_non_positive() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        String name = "Conference Room";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, name, 0));
        assertEquals("La capacité doit être un nombre positif.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_name_is_null_or_blank() {
        // Arrange
        RoomId roomId = new RoomId("Room1");

        // Act & Assert for null name
        IllegalArgumentException exceptionNull = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, null, 20));
        assertEquals("Le nom ne peut pas être null ou vide.", exceptionNull.getMessage());

        // Act & Assert for blank name
        IllegalArgumentException exceptionBlank = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, "   ", 20));
        assertEquals("Le nom ne peut pas être null ou vide.", exceptionBlank.getMessage());
    }

    @Test
    void should_return_correct_id_from_get_id() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        Room room = new Room(roomId, "Conference Room", 20);

        // Act
        String id = room.getId();

        // Assert
        assertEquals("Room1", id);
    }

    @Test
    void should_return_correct_string_representation() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        Room room = new Room(roomId, "Conference Room", 20);

        // Act
        String roomString = room.toString();

        // Assert
        assertEquals("Salle : Conference Room (Cap. 20)\n", roomString);
    }

    @Test
    void should_return_true_when_two_rooms_are_equal() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        Room room1 = new Room(roomId, "Conference Room", 20);
        Room room2 = new Room(roomId, "Conference Room", 20);

        // Act & Assert
        assertEquals(room1, room2);
    }

    @Test
    void should_return_false_when_two_rooms_have_different_ids_or_names() {
        // Arrange
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");
        Room room1 = new Room(roomId1, "Conference Room", 20);
        Room room2 = new Room(roomId2, "Conference Room", 20);
        Room room3 = new Room(roomId1, "Different Room", 20);

        // Act & Assert
        assertNotEquals(room1, room2);
        assertNotEquals(room1, room3);
    }

    @Test
    void should_return_same_hash_code_for_identical_rooms() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        Room room1 = new Room(roomId, "Conference Room", 20);
        Room room2 = new Room(roomId, "Conference Room", 20);

        // Act & Assert
        assertEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    void should_return_different_hash_codes_for_different_rooms() {
        // Arrange
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");
        Room room1 = new Room(roomId1, "Conference Room", 20);
        Room room2 = new Room(roomId2, "Conference Room", 20);

        // Act & Assert
        assertNotEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    void testIsValid_CapacityChecks() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        Room room = new Room(roomId, "Salle A", 20);

        // Act & Assert
        assertTrue(room.isValid(10));   // Valid: within capacity
        assertTrue(room.isValid(20));   // Valid: exactly at capacity
        IllegalArgumentException exceptionNegative = assertThrows(IllegalArgumentException.class,
                () -> room.isValid(-5)); // Invalid: negative persons
        assertEquals("Le nombre de personnes attendues ne peut pas être négatif.", exceptionNegative.getMessage());
    }

    @Test
    void testEqualsAndHashCode_DifferentCases() {
        // Arrange
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");
        Room room1 = new Room(roomId1, "Room A", 10);
        Room room2 = new Room(roomId1, "Room A", 10);
        Room room3 = new Room(roomId2, "Room B", 15);

        // Act & Assert
        assertEquals(room1, room2);
        assertNotEquals(room1, room3);
        assertEquals(room1.hashCode(), room2.hashCode());
        assertNotEquals(room1.hashCode(), room3.hashCode());
    }

    @Test
    void should_throw_exception_when_name_is_empty_string() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        String name = ""; // Empty string
        int capacity = 20;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, name, capacity));
        assertEquals("Le nom ne peut pas être null ou vide.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_name_is_spaces_only() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        String name = "   "; // Spaces only
        int capacity = 20;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, name, capacity));
        assertEquals("Le nom ne peut pas être null ou vide.", exception.getMessage());
    }

    @Test
    void testEquals_NullAndOtherTypes() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        Room room = new Room(roomId, "Salle A", 20);

        // Act & Assert
        assertNotEquals(room, null); // Comparaison avec null
        assertNotEquals(room, "Not a Room"); // Comparaison avec un autre type
    }

}
