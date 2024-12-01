package mma.admin.domains;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendaTest {

    @Test
    void should_throw_exception_when_room_is_null() {
        // Arrange
        Room room = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Agenda(room));
        assertEquals("Room cannot be null.", exception.getMessage());
    }

    @Test
    void should_add_reservation_successfully() {
        // Arrange
        Room room = mock(Room.class);
        Agenda agenda = new Agenda(room);
        Reservation reservation = mock(Reservation.class);

        // Act
        agenda.add(reservation);

        // Assert
        assertTrue(agenda.getReservations().contains(reservation));
    }

    @Test
    void should_return_correct_room() {
        // Arrange
        Room room = mock(Room.class);
        Agenda agenda = new Agenda(room);

        // Act
        Room result = agenda.getRoom();

        // Assert
        assertEquals(room, result);
    }

    @Test
    void should_return_empty_reservations_initially() {
        // Arrange
        Room room = mock(Room.class);
        Agenda agenda = new Agenda(room);

        // Act
        Set<Reservation> reservations = agenda.getReservations();

        // Assert
        assertTrue(reservations.isEmpty());
    }

    @Test
    void given_reservations_when_get_by_date_then_return_correct_reservations() {
        // Arrange
        Room room = mock(Room.class);
        Agenda agenda = new Agenda(room);
        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);
        LocalDate date = LocalDate.now();

        when(reservation1.isDate(date)).thenReturn(true);
        when(reservation2.isDate(date)).thenReturn(false);

        agenda.add(reservation1);
        agenda.add(reservation2);

        // Act
        List<Reservation> result = agenda.getReservationsByDate(date);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(reservation1));
    }

    @Test
    void given_time_slots_when_get_ligne_menu_then_return_correct_string() {
        // Arrange
        Room room = mock(Room.class);
        when(room.getId()).thenReturn("Room1");
        Agenda agenda = new Agenda(room);
        Reservation reservation = mock(Reservation.class);
        LocalDate date = LocalDate.now();
        LocalTime[] timeSlots = { LocalTime.of(9, 0), LocalTime.of(10, 0) };

        when(reservation.isDate(date)).thenReturn(true);
        when(reservation.isInTimeSlot(LocalTime.of(9, 0))).thenReturn(true);
        when(reservation.isInTimeSlot(LocalTime.of(10, 0))).thenReturn(false);

        agenda.add(reservation);

        // Act
        String ligneMenu = agenda.getLigneMenu(timeSlots, date, "*");

        // Assert
        assertEquals("Room1     *         ", ligneMenu);
    }

    @Test
    void given_empty_reservations_when_search_availabilities_then_generate_availabilities() {
        // Arrange
        Room room = mock(Room.class);
        when(room.capacity()).thenReturn(20);
        Agenda agenda = new Agenda(room);
        LocalDate date = LocalDate.now();
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        Duration duration = Duration.ofMinutes(30);

        // Act
        agenda.searchAvailabilities(timeSlot, date, duration, 10);

        // Assert
        assertFalse(agenda.getAvailabilities().isEmpty());
    }

    @Test
    void given_existing_reservations_when_search_availabilities_then_calculate_correctly() {
        // Arrange
        Room room = mock(Room.class);
        when(room.capacity()).thenReturn(20);
        Agenda agenda = new Agenda(room);
        Reservation reservation = mock(Reservation.class);
        LocalDate date = LocalDate.now();
        TimeSlot wh = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        Duration duration = Duration.ofMinutes(30);

        when(reservation.isOverlay(any())).thenReturn(true);
        when(reservation.getTimeSlot()).thenReturn(new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        when(reservation.isDate(date)).thenReturn(true);

        agenda.add(reservation);

        // Act
        agenda.searchAvailabilities(wh, date, duration, 10);

        // Assert
        assertFalse(agenda.getAvailabilities().isEmpty());
    }

    @Test
    void should_return_empty_availabilities_initially() {
        // Arrange
        Room room = mock(Room.class);
        Agenda agenda = new Agenda(room);

        // Act
        Set<Availability> availabilities = agenda.getAvailabilities();

        // Assert
        assertTrue(availabilities.isEmpty());
    }
}

