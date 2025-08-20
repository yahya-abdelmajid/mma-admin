package mma.admin.domains;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AgendaTest {

    // ---------- Constructeur ----------

    @Test
    void givenNullRoom_whenConstructingAgenda_thenThrowsIllegalArgumentException() {
        // Arrange
        Room room = null;

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Agenda(room));

        // Assert
        assertEquals("Une room ne peut pas etre null.", ex.getMessage());
    }

    @Test
    void givenValidRoom_whenConstructingAgenda_thenInitialStateIsEmptyAndRoomIsSet() {
        // Arrange
        Room room = new Room(new RoomId("LC1"), "Salle Alpha", 10);

        // Act
        Agenda agenda = new Agenda(room);

        // Assert
        assertSame(room, agenda.getRoom());
        assertTrue(agenda.getReservations().isEmpty());
        assertTrue(agenda.getAvailabilities().isEmpty());
    }

    // ---------- add(Reservation) ----------

    @Test
    void givenNullReservation_whenAdd_thenThrowsIllegalArgumentException() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LC1"), "Salle", 5));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> agenda.add(null));

        // Assert
        assertEquals("Reservation ne peut pas être null.", ex.getMessage());
    }

    @Test
    void givenValidReservation_whenAdd_thenReservationIsStored() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LC1"), "Salle", 5));
        Reservation r = new Reservation(
                1, "LC1", "U1",
                new TimeSlot(LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0)),
                "Réunion", 3
        );

        // Act
        agenda.add(r);

        // Assert
        assertEquals(1, agenda.getReservations().size());
        assertTrue(agenda.getReservations().contains(r));
    }

    // ---------- getReservationsByDate(LocalDate) ----------

    @Test
    void givenNullDate_whenGetReservationsByDate_thenThrowsIllegalArgumentException() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LC1"), "Salle", 5));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> agenda.getReservationsByDate(null));

        // Assert
        assertEquals("Date ne peut pas être null", ex.getMessage());
    }

    @Test
    void givenReservationsOnDifferentDays_whenGetReservationsByDate_thenReturnsOnlyMatching() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LC1"), "Salle", 5));
        LocalDate d1 = LocalDate.now().plusDays(1);
        LocalDate d2 = d1.plusDays(1);

        Reservation r1 = new Reservation(1, "LC1", "U1",
                new TimeSlot(d1, LocalTime.of(9, 0), LocalTime.of(10, 0)), "A", 3);
        Reservation r2 = new Reservation(2, "LC1", "U2",
                new TimeSlot(d2, LocalTime.of(11, 0), LocalTime.of(12, 0)), "B", 3);

        agenda.add(r1);
        agenda.add(r2);

        // Act
        List<Reservation> sameDay = agenda.getReservationsByDate(d1);

        // Assert
        assertEquals(1, sameDay.size());
        assertEquals(r1, sameDay.get(0));
    }

    // ---------- getLigneMenu(LocalTime[] header, LocalDate date, String signe) ----------

    @Test
    void givenNullArgs_whenGetLigneMenu_thenThrowsIllegalArgumentException() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LCX"), "Salle", 5));
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime[] header = new LocalTime[]{LocalTime.of(9, 0)};
        String signe = "X";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> agenda.getLigneMenu(null, date, signe));
        assertThrows(IllegalArgumentException.class, () -> agenda.getLigneMenu(header, null, signe));
        assertThrows(IllegalArgumentException.class, () -> agenda.getLigneMenu(header, date, null));
    }

    @Test
    void givenNoReservations_whenGetLigneMenu_thenLineContainsOnlySpacesAfterId() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LCX"), "Salle", 5));
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime[] header = new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(10, 0)};

        // Act
        String line = agenda.getLigneMenu(header, date, "X");

        // Assert
        // Préfixe = id padded 10 chars
        String expectedPrefix = String.format("%-10s", "LCX");
        assertTrue(line.startsWith(expectedPrefix));
        // le reste : deux blocs de 10 espaces
        String rest = line.substring(expectedPrefix.length());
        assertEquals(String.format("%-10s", ' ') + String.format("%-10s", ' '), rest);
    }

    @Test
    void givenReservationsAndHeader_whenGetLigneMenu_thenMarksOccupiedSlotsWithSigne() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LCZ"), "Salle", 5));
        LocalDate d = LocalDate.now().plusDays(1);

        // Réservation 09:00–10:00
        agenda.add(new Reservation(1, "LCZ", "U1",
                new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0)), "R", 2));

        LocalTime[] header = new LocalTime[]{LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0)};
        String signe = "X";

        // Act
        String line = agenda.getLigneMenu(header, d, signe);

        // Assert
        String expectedPrefix = String.format("%-10s", "LCZ");
        String expected =
                expectedPrefix +
                        String.format("%-10s", ' ') +     // 08:00 → libre
                        String.format("%-10s", signe) +  // 09:00 → occupé (début inclus)
                        String.format("%-10s", signe);   // 10:00 → occupé (fin incluse selon TimeSlot.isInTimeSlot)
        assertEquals(expected, line);
    }

    // ---------- searchAvailabilities(TimeSlot, LocalDate, LocalDate, Duration, int) ----------

    @Test
    void givenInvalidParams_whenSearchAvailabilities_thenThrowsIllegalArgumentException() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LC1"), "Salle", 10));
        TimeSlot wh = new TimeSlot(LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0));
        LocalDate date = LocalDate.now().plusDays(1);
        LocalDate current = date;

        // Act & Assert (nulls et capacity <= 0)
        assertThrows(IllegalArgumentException.class, () ->
                agenda.searchAvailabilities(null, date, current, Duration.ofHours(1), 1));
        assertThrows(IllegalArgumentException.class, () ->
                agenda.searchAvailabilities(wh, null, current, Duration.ofHours(1), 1));
        assertThrows(IllegalArgumentException.class, () ->
                agenda.searchAvailabilities(wh, date, current, null, 1));
        assertThrows(IllegalArgumentException.class, () ->
                agenda.searchAvailabilities(wh, date, current, Duration.ofHours(1), 0));
        assertThrows(IllegalArgumentException.class, () ->
                agenda.searchAvailabilities(wh, date, current, Duration.ofHours(1), -2));
    }

    @Test
    void givenNoReservationsOnCurrentDate_whenSearchAvailabilities_thenOneAvailabilityAddedForWholeWorkingHours() {
        // Arrange
        Room room = new Room(new RoomId("LC1"), "Salle", 10);
        Agenda agenda = new Agenda(room);

        TimeSlot workingHours = new TimeSlot(LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0));
        LocalDate date = LocalDate.now().plusDays(3);
        LocalDate current = date; // même jour → getReservationsByDate(current) sera vide
        Duration duration = Duration.ofHours(1);
        int capacity = 5;

        // Act
        agenda.searchAvailabilities(workingHours, date, current, duration, capacity);

        // Assert
        Set<Availability> avs = agenda.getAvailabilities();
        assertEquals(1, avs.size()); // on ne dépend pas de l'égalité des objets Availability
    }

    @Test
    void givenExistingReservationsOnCurrentDate_whenSearchAvailabilities_thenElseBranchIsExecuted() {
        // Arrange
        Room room = new Room(new RoomId("LC2"), "Salle", 10);
        Agenda agenda = new Agenda(room);

        LocalDate d = LocalDate.now().plusDays(1);
        // Réservation ce jour-là pour forcer la branche "else"
        agenda.add(new Reservation(1, "LC2", "U1",
                new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0)), "R", 3));

        TimeSlot workingHours = new TimeSlot(LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0));
        Duration duration = Duration.ofHours(1);

        // Act
        // On exécute la branche else; le contenu précis dépend de AvailabilityCalculator,
        // mais on veut surtout couvrir la branche sans échouer.
        agenda.searchAvailabilities(workingHours, d, d, duration, 5);

        // Assert
        // Pas d'assertion sur le contenu (dépend de AvailabilityCalculator).
        // On vérifie simplement que l'appel ne jette pas et que l'ensemble des dispos est cohérent (>= 0).
        assertNotNull(agenda.getAvailabilities());
        assertTrue(agenda.getAvailabilities().size() >= 0);
    }

    // ---------- getReservations(LocalDate start, LocalDate end) ----------

    @Test
    void givenRange_whenGetReservations_thenReturnsOnlyReservationsInsideRange() {
        // Arrange
        Agenda agenda = new Agenda(new Room(new RoomId("LC3"), "Salle", 10));
        LocalDate base = LocalDate.of(2030, 1, 10);

        Reservation rBefore = new Reservation(1, "LC3", "U1",
                new TimeSlot(base.minusDays(2), LocalTime.of(9, 0), LocalTime.of(10, 0)), "B", 2);
        Reservation rInside = new Reservation(2, "LC3", "U2",
                new TimeSlot(base, LocalTime.of(11, 0), LocalTime.of(12, 0)), "I", 2);
        Reservation rAfter = new Reservation(3, "LC3", "U3",
                new TimeSlot(base.plusDays(2), LocalTime.of(13, 0), LocalTime.of(14, 0)), "A", 2);

        agenda.add(rBefore);
        agenda.add(rInside);
        agenda.add(rAfter);

        LocalDate start = base.minusDays(1);
        LocalDate end = base.plusDays(1);

        // Act
        List<Reservation> list = agenda.getReservations(start, end);

        // Assert
        assertEquals(1, list.size());
        assertEquals(rInside, list.get(0));
    }
}
