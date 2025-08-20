package mma.admin.domains;

import mma.admin.domains.exceptions.NoPositiveCapacityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void givenNullRoomId_whenConstructingRoom_thenThrowsIllegalArgumentExceptionWithMessage() {
        // Arrange
        RoomId nullId = null;

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Room(nullId, "Salle Alpha", 10));

        // Assert
        assertEquals("L'id de la salle ne peut pas être null.", ex.getMessage());
    }

    @Test
    void givenNullName_whenConstructingRoom_thenThrowsIllegalArgumentExceptionWithMessage() {
        // Arrange
        RoomId id = new RoomId("LC1");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Room(id, null, 10));

        // Assert
        assertEquals("Le nom ne peut pas être null ou vide.", ex.getMessage());
    }

    @Test
    void givenBlankOrEmptyName_whenConstructingRoom_thenThrowsIllegalArgumentExceptionWithMessage() {
        // Arrange
        RoomId id = new RoomId("LC1");

        // Act
        IllegalArgumentException exBlank = assertThrows(IllegalArgumentException.class,
                () -> new Room(id, "   ", 10));
        IllegalArgumentException exEmpty = assertThrows(IllegalArgumentException.class,
                () -> new Room(id, "", 10));

        // Assert
        assertEquals("Le nom ne peut pas être null ou vide.", exBlank.getMessage());
        assertEquals("Le nom ne peut pas être null ou vide.", exEmpty.getMessage());
    }

    @Test
    void givenNonPositiveCapacity_whenConstructingRoom_thenThrowsIllegalArgumentExceptionWithMessage() {
        // Arrange
        RoomId id = new RoomId("LC1");

        // Act
        IllegalArgumentException exZero = assertThrows(IllegalArgumentException.class,
                () -> new Room(id, "Salle Alpha", 0));
        IllegalArgumentException exNegative = assertThrows(IllegalArgumentException.class,
                () -> new Room(id, "Salle Alpha", -5));

        // Assert
        assertEquals("La capacité doit être un nombre positif.", exZero.getMessage());
        assertEquals("La capacité doit être un nombre positif.", exNegative.getMessage());
    }

    // ---------- Accesseurs record & getId & toString ----------

    @Test
    void givenValidArgs_whenConstructingRoom_thenAccessorsAndGetIdAndToStringAreCorrect() {
        // Arrange
        RoomId id = new RoomId("LB2");

        // Act
        Room room = new Room(id, "Salle Beta", 12);

        // Assert
        assertEquals(id, room.roomId());
        assertEquals("Salle Beta", room.name());
        assertEquals(12, room.capacity());
        assertEquals("LB2", room.getId());
        assertEquals("Salle : Salle Beta (Cap. 12)\n", room.toString());
    }

    // ---------- isValid (branches vrai/faux/exception + bornes) ----------

    @Test
    void givenExpectedPersonsWithinCapacity_whenIsValid_thenReturnsTrue() {
        // Arrange
        Room room = new Room(new RoomId("LC1"), "Salle Alpha", 10);

        // Act
        boolean ok1 = room.isValid(1);
        boolean ok2 = room.isValid(10); // borne = capacité

        // Assert
        assertTrue(ok1);
        assertTrue(ok2);
    }

    @Test
    void givenExpectedPersonsExceedingCapacity_whenIsValid_thenReturnsFalse() {
        // Arrange
        Room room = new Room(new RoomId("LC1"), "Salle Alpha", 10);

        // Act
        boolean result = room.isValid(11);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenNonPositiveExpectedPersons_whenIsValid_thenThrowsNoPositiveCapacityExceptionWithMessage() {
        // Arrange
        Room room = new Room(new RoomId("LC1"), "Salle Alpha", 10);

        // Act
        NoPositiveCapacityException exZero = assertThrows(NoPositiveCapacityException.class, () -> room.isValid(0));
        NoPositiveCapacityException exNegative = assertThrows(NoPositiveCapacityException.class, () -> room.isValid(-3));

        // Assert
        assertEquals("Le nombre de personnes attendues ne peut pas être négatif.", exZero.getMessage());
        assertEquals("Le nombre de personnes attendues ne peut pas être négatif.", exNegative.getMessage());
    }

    // ---------- equals / hashCode de record (comportement standard) ----------

    @Test
    void givenIdenticalRooms_whenEquals_thenTrueAndHashCodesMatch() {
        // Arrange
        Room a = new Room(new RoomId("LC1"), "Salle Alpha", 10);
        Room b = new Room(new RoomId("LC1"), "Salle Alpha", 10);

        // Act
        boolean eq = a.equals(b);
        int ha = a.hashCode();
        int hb = b.hashCode();

        // Assert
        assertTrue(eq);
        assertEquals(ha, hb);
    }

    @Test
    void givenDifferentRooms_whenEquals_thenFalseAndHashCodesLikelyDiffer() {
        // Arrange
        Room a = new Room(new RoomId("LC1"), "Salle Alpha", 10);
        Room b = new Room(new RoomId("LC2"), "Salle Beta", 8);

        // Act
        boolean eq = a.equals(b);

        // Assert
        assertFalse(eq);
        // Pas d'obligation contractuelle que les hashCodes diffèrent, donc on n'assert pas ici
    }

    @Test
    void givenRoom_whenEqualsWithNullOrDifferentType_thenReturnsFalse() {
        // Arrange
        Room a = new Room(new RoomId("LC1"), "Salle Alpha", 10);

        // Act
        boolean eqNull = a.equals(null);
        boolean eqOtherType = a.equals("not-a-room");

        // Assert
        assertFalse(eqNull);
        assertFalse(eqOtherType);
    }
}
