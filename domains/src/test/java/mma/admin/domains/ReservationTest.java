package mma.admin.domains;

import mma.admin.domains.exceptions.NoPositiveCapacityException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservationTest {

    @Test
    public void should_return_correct_values_for_getters_and_setters() {
        RoomId roomId = new RoomId("Room1");
        String personId = "12345";
        LocalDateTime date = LocalDateTime.of(2023, 10, 10, 10, 0);
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));
        String description = "Meeting";

        Reservation reservation = new Reservation(1,"Room1", personId, timeSlot, description,10);

        assertEquals(roomId, reservation.getRoomId());
        assertEquals(personId, reservation.getPersonId());
        assertEquals(timeSlot, reservation.getTimeSlot());
        assertEquals(description, reservation.getDescription());

        RoomId newRoomId = new RoomId("Room2");
        LocalDateTime newDate = LocalDateTime.of(2023, 10, 11, 11, 0);
        Reservation reservation2 = new Reservation(1,"Room2", "54321", timeSlot, "Updated meeting",10);

        assertEquals(newRoomId, reservation2.getRoomId());
        assertEquals("54321", reservation2.getPersonId());
        assertEquals("Updated meeting", reservation2.getDescription());
    }

    @Test
    public void it_should_return_true_if_date_matches_reservation_date() {
        LocalDate date = LocalDate.of(2023, 10, 10);
        TimeSlot timeSlot = new TimeSlot(date, LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation reservation = new Reservation(1,"Room1", "12345", timeSlot, "Meeting",10);

        assertTrue(reservation.isDate(date));
        assertFalse(reservation.isDate(LocalDate.of(2023, 10, 11)));
    }

    @Test
    public void should_return_true_if_time_is_within_timeslot() {
        LocalDate date = LocalDate.of(2023, 10, 10);
        TimeSlot timeSlot = new TimeSlot(date, LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation reservation = new Reservation(1,"Room1", "12345", timeSlot, "Meeting",10);

        assertTrue(reservation.isInTimeSlot(LocalTime.of(10, 30)));
        assertFalse(reservation.isInTimeSlot(LocalTime.of(9, 30)));
    }

    @Test
    public void given_reservation_when_to_string_called_then_correct_format_should_be_returned() {
        RoomId roomId = new RoomId("Room1");
        String personId = "12345";
        LocalDateTime date = LocalDateTime.of(2023, 10, 10, 10, 0);
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));
        String description = "Meeting";

        Reservation reservation = new Reservation(1,"Room1", personId, timeSlot, description,10);

        String expected = String.format("%s, de %s à %s\nDescription : %s\n",
                date.toLocalDate(), timeSlot.getStart(), timeSlot.getEnd(), description);

        assertEquals(expected, reservation.toString());
    }

    @Test
    public void given_two_reservations_when_equals_and_hash_code_called_then_should_be_equal_if_same_values() {
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));

        Reservation reservation1 = new Reservation(1,"Room1", "12345", timeSlot, "Meeting",10);
        Reservation reservation2 = new Reservation(2,"Room2", "12345", timeSlot, "Meeting",10);
        Reservation reservation3 = new Reservation(3,"Room2", "67890", timeSlot, "Workshop",10);

        assertEquals(reservation1, reservation1);
        assertNotEquals(reservation1, reservation3);
        assertEquals(reservation1.hashCode(), reservation1.hashCode());
        assertNotEquals(reservation1.hashCode(), reservation3.hashCode());
    }

    @Test
    public void given_two_reservations_when_compare_to_called_then_should_return_correct_comparison_value() {
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");

        TimeSlot timeSlot1 = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(11, 0), LocalTime.of(13, 0));

        Reservation reservation1 = new Reservation(1,"Room1", "12345", timeSlot1, "Meeting",10);
        Reservation reservation2 = new Reservation(2,"Room2", "12345", timeSlot2, "Meeting",10);
        Reservation reservation3 = new Reservation(3,"Room2", "67890", timeSlot1, "Workshop",10);

        assertTrue(reservation1.compareTo(reservation2) < 0);
        assertTrue(reservation2.compareTo(reservation1) > 0);
        assertTrue(reservation1.compareTo(reservation3) < 0);
        assertTrue(reservation1.compareTo(reservation1) == 0);
    }

    @Test
    public void should_throw_exception_for_null_roomId_or_timeSlot_in_constructor() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> new Reservation(1, null, "12345", null, "Réunion",10));
        assertEquals("roomId ne peut pas être nul ou vide.", exception1.getMessage());

        NullPointerException exception2 = assertThrows(NullPointerException.class,
                () -> new Reservation(1, "Salle1", "12345", null, "Réunion",10));
        assertEquals("timeSlot ne peut pas être nul.", exception2.getMessage());

    }

    @Test
    public void should_throw_exception_for_null_values_in_methods() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation reservation = new Reservation(1, "Salle1", "12345", timeSlot, "Réunion",10);

        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> reservation.isOverlay(null));
        assertEquals("Le créneau horaire fourni ne peut pas être nul.", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> reservation.addPrestation(null));
        assertEquals("La prestation ajoutée ne peut pas être nulle.", exception2.getMessage());

        IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class,
                () -> reservation.compareTo(null));
        assertEquals("Impossible de comparer avec une réservation nulle.", exception3.getMessage());
    }

    @Test
    public void prestationsToString_should_return_correct_string_for_empty_and_non_empty_lists() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation reservation = new Reservation(1, "Salle1", "12345", timeSlot, "Réunion",10);

        assertEquals("Aucun service prévu.", reservation.prestationsToString());

        reservation.addPrestation(new Prestation(1,"Service de restauration"));
        reservation.addPrestation(new Prestation(2,"Configuration audiovisuelle"));

        assertEquals("Services prévus : Service de restauration, Configuration audiovisuelle", reservation.prestationsToString());
    }

    @Test
    void testConstructor_ValidParameters() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1, "Room A", "P123", timeSlot, "Description",10);

        assertNotNull(reservation);
        assertEquals(1, reservation.getId());
        assertEquals("Room A", reservation.getRoomId().id());
        assertEquals("P123", reservation.getPersonId());
        assertEquals("Description", reservation.getDescription());
        assertEquals(timeSlot, reservation.getTimeSlot());
    }

    @Test
    void testConstructor_InvalidRoomId() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1, null, "P123", timeSlot, "Description",10);
        });
        assertEquals("roomId ne peut pas être nul ou vide.", exception.getMessage());
    }

    @Test
    void testConstructor_InvalidPersonId() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1, "Room A", "", timeSlot, "Description",10);
        });
        assertEquals("personId ne peut pas être nul ou vide.", exception.getMessage());
    }

    @Test
    void testConstructor_DescriptionTooLong() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1, "Room A", "P123", timeSlot, "A".repeat(501),10);
        });
        assertEquals("La description ne peut pas dépasser 500 caractères.", exception.getMessage());
    }

    @Test
    void testAddPrestation_ValidPrestation() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1, "Room A", "P123", timeSlot, "Description",10);

        Prestation prestation = new Prestation(1,"Service 1");
        reservation.addPrestation(prestation);

        assertEquals(1, reservation.getPrestations().size());
        assertTrue(reservation.getPrestations().contains(prestation));
    }

    @Test
    void testAddPrestation_NullPrestation() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1, "Room A", "P123", timeSlot, "Description",10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservation.addPrestation(null);
        });
        assertEquals("La prestation ajoutée ne peut pas être nulle.", exception.getMessage());
    }

    @Test
    void testAddPrestation_DuplicatePrestation() {
        TimeSlot timeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1, "Room A", "P123", timeSlot, "Description",10);

        Prestation prestation = new Prestation(1,"Service 1");
        reservation.addPrestation(prestation);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservation.addPrestation(prestation); // Adding the same prestation again
        });
        assertEquals("Cette prestation est déjà ajoutée.", exception.getMessage());
    }

    @Test
    void testEquals_True() {
        TimeSlot timeSlot1 = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reservation reservation1 = new Reservation(1, "Room A", "P123", timeSlot1, "Description",10);
        Reservation reservation2 = new Reservation(1, "Room A", "P124", timeSlot2, "Description",10);

        assertTrue(reservation1.equals(reservation2));
    }

    @Test
    void testEquals_False() {
        TimeSlot timeSlot1 = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0));

        Reservation reservation1 = new Reservation(1, "Room A", "P123", timeSlot1, "Description",10);
        Reservation reservation2 = new Reservation(2, "Room A", "P124", timeSlot2, "Description",10);

        assertFalse(reservation1.equals(reservation2));
    }

    @Test
    void testCompareTo_LessThan() {
        TimeSlot timeSlot1 = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0));

        Reservation reservation1 = new Reservation(1, "Room A", "P123", timeSlot1, "Description",10);
        Reservation reservation2 = new Reservation(2, "Room A", "P124", timeSlot2, "Description",10);

        assertTrue(reservation1.compareTo(reservation2) < 0); // reservation1 starts before reservation2
    }

    @Test
    void testCompareTo_GreaterThan() {
        TimeSlot timeSlot1 = new TimeSlot(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reservation reservation1 = new Reservation(1, "Room A", "P123", timeSlot1, "Description",10);
        Reservation reservation2 = new Reservation(2, "Room A", "P124", timeSlot2, "Description",10);

        assertTrue(reservation1.compareTo(reservation2) > 0); // reservation1 starts after reservation2
    }

    @Test
    void testCompareTo_SameTimeSlot() {
        TimeSlot timeSlot1 = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reservation reservation1 = new Reservation(1, "LC1", "P123", timeSlot1, "Description",10);
        Reservation reservation2 = new Reservation(2, "LC1", "P124", timeSlot1, "Description",10);

        assertTrue(reservation1.compareTo(reservation2) == 0); // reservation1 has lower id
    }

    @Test
    void testRequirePositive_ok() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        Reservation r = new Reservation(1, "LC1", "U123", slot, "Test", 5);

        assertEquals(5, r.getExpectedPerson());
    }

    @Test
    void testRequirePositive_throwsExceptionWhenZero() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        assertThrows(NoPositiveCapacityException.class,
                () -> new Reservation(1, "LC1", "U123", slot, "Test", 0));
    }

    @Test
    void testRequirePositive_throwsExceptionWhenNegative() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        assertThrows(NoPositiveCapacityException.class,
                () -> new Reservation(1, "LC1", "U123", slot, "Test", -3));
    }
}
