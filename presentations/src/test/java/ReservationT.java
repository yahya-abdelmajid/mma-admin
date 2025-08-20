import mma.admin.presentations.ReservationT;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTTest {

    @Test
    void givenValidValues_whenCreateReservationDTO_thenFieldsAreAccessible() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2030, 1, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2030, 1, 1, 10, 0);

        // Act
        ReservationT dto = new ReservationT("LC1", "U1", start, end, "Réunion", 5, "Café");

        // Assert
        assertEquals("LC1", dto.roomId());
        assertEquals("U1", dto.matricule());
        assertEquals(start, dto.start());
        assertEquals(end, dto.end());
        assertEquals("Réunion", dto.description());
        assertEquals(5, dto.expected());
        assertEquals("Café", dto.services());
    }

    @Test
    void givenTwoIdenticalDTOs_whenCompare_thenTheyAreEqualAndHaveSameHashCode() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2030, 1, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2030, 1, 1, 10, 0);

        ReservationT dto1 = new ReservationT("LC1", "U1", start, end, "Réunion", 5, "Café");
        ReservationT dto2 = new ReservationT("LC1", "U1", start, end, "Réunion", 5, "Café");

        // Act & Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void givenDifferentDTOs_whenCompare_thenTheyAreNotEqual() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2030, 1, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2030, 1, 1, 10, 0);

        ReservationT dto1 = new ReservationT("LC1", "U1", start, end, "Réunion", 5, "Café");
        ReservationT dto2 = new ReservationT("LC2", "U2", start, end, "Daily", 2, "Projecteur");

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void givenDTO_whenToString_thenStringContainsFieldValues() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2030, 1, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2030, 1, 1, 10, 0);
        ReservationT dto = new ReservationT("LC1", "U1", start, end, "Réunion", 5, "Café");

        // Act
        String str = dto.toString();

        // Assert
        assertTrue(str.contains("LC1"));
        assertTrue(str.contains("U1"));
        assertTrue(str.contains("Réunion"));
        assertTrue(str.contains("Café"));
    }
}

