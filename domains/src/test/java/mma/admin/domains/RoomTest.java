package mma.admin.domains;
import mma.admin.domains.RoomId;
import mma.admin.domains.Reservation;
import mma.admin.domains.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void should_throw_exception_when_room_id_is_null() {
        // Arrange
        RoomId roomId = null;
        String name = "Conference Room";
        int capacity = 20;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, name, capacity));
        assertEquals("L'id de la salle ne peut pas être null.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_name_is_null() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        String name = null;
        int capacity = 20;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, name, capacity));
        assertEquals("Le nom ne peut pas être null.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_name_is_blank() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        String name = "  ";
        int capacity = 20;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, name, capacity));
        assertEquals("Le nom ne peut pas être null.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_capacity_is_non_positive() {
        // Arrange
        RoomId roomId = new RoomId("Room1");
        String name = "Conference Room";
        int capacity = 0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Room(roomId, name, capacity));
        assertEquals("La capacité doit être positive.", exception.getMessage());
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
    void should_return_false_when_two_rooms_have_different_ids() {
        // Arrange
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");
        Room room1 = new Room(roomId1, "Conference Room", 20);
        Room room2 = new Room(roomId2, "Conference Room", 20);

        // Act & Assert
        assertNotEquals(room1, room2);
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
}

