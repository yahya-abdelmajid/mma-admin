package mma.admin.domains;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationTest {

    @Test
    public void should_return_correct_values_for_getters_and_setters() {
        RoomId roomId = new RoomId("Room1");
        String personId = "12345";
        LocalDateTime date = LocalDateTime.of(2023, 10, 10, 10, 0);
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));
        String description = "Meeting";

        Reservation reservation = new Reservation(roomId, personId, date, timeSlot, description);

        assertEquals(roomId, reservation.getRoomId());
        assertEquals(personId, reservation.getPersonId());
        assertEquals(date, reservation.getDate());
        assertEquals(timeSlot, reservation.getTimeSlot());
        assertEquals(description, reservation.getDescription());

        RoomId newRoomId = new RoomId("Room2");
        reservation.setRoomId(newRoomId);
        reservation.setPersonId("54321");
        reservation.setDate(LocalDateTime.of(2023, 10, 11, 11, 0));
        reservation.setTimeSlot(new TimeSlot(LocalDate.of(2023, 10, 11), LocalTime.of(11, 0), LocalTime.of(13, 0)));
        reservation.setDescription("Updated meeting");

        assertEquals(newRoomId, reservation.getRoomId());
        assertEquals("54321", reservation.getPersonId());
        assertEquals(LocalDateTime.of(2023, 10, 11, 11, 0), reservation.getDate());
        assertEquals("Updated meeting", reservation.getDescription());
    }

    @Test
    public void it_should_return_true_if_date_matches_reservation_date() {
        LocalDate date = LocalDate.of(2023, 10, 10);
        TimeSlot timeSlot = new TimeSlot(date, LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation reservation = new Reservation(new RoomId("Room1"), "12345", LocalDateTime.now(), timeSlot, "Meeting");

        assertTrue(reservation.isDate(date));
        assertFalse(reservation.isDate(LocalDate.of(2023, 10, 11)));
    }

    @Test
    public void should_return_true_if_time_is_within_timeslot() {
        LocalDate date = LocalDate.of(2023, 10, 10);
        TimeSlot timeSlot = new TimeSlot(date, LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation reservation = new Reservation(new RoomId("Room1"), "12345", LocalDateTime.now(), timeSlot, "Meeting");

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

        Reservation reservation = new Reservation(roomId, personId, date, timeSlot, description);

        String expected = "Reservation{roomId=Room1, personId='12345', date=2023-10-10T10:00, timeSlot=" + timeSlot + ", description='Meeting'}";
        assertEquals(expected, reservation.toString());
    }

    @Test
    public void given_two_reservations_when_equals_and_hash_code_called_then_should_be_equal_if_same_values() {
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));

        Reservation reservation1 = new Reservation(roomId1, "12345", LocalDateTime.now(), timeSlot, "Meeting");
        Reservation reservation2 = new Reservation(roomId1, "12345", LocalDateTime.now(), timeSlot, "Meeting");
        Reservation reservation3 = new Reservation(roomId2, "67890", LocalDateTime.now(), timeSlot, "Workshop");

        assertEquals(reservation1, reservation2);
        assertNotEquals(reservation1, reservation3);
        assertEquals(reservation1.hashCode(), reservation2.hashCode());
        assertNotEquals(reservation1.hashCode(), reservation3.hashCode());
    }

    @Test
    public void given_two_reservations_when_compare_to_called_then_should_return_correct_comparison_value() {
        RoomId roomId1 = new RoomId("Room1");
        RoomId roomId2 = new RoomId("Room2");

        TimeSlot timeSlot1 = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.of(2023, 10, 10), LocalTime.of(11, 0), LocalTime.of(13, 0));

        Reservation reservation1 = new Reservation(roomId1, "12345", LocalDateTime.now(), timeSlot1, "Meeting");
        Reservation reservation2 = new Reservation(roomId1, "12345", LocalDateTime.now(), timeSlot2, "Meeting");
        Reservation reservation3 = new Reservation(roomId2, "67890", LocalDateTime.now(), timeSlot1, "Workshop");

        assertTrue(reservation1.compareTo(reservation2) < 0);
        assertTrue(reservation2.compareTo(reservation1) > 0);
        assertTrue(reservation1.compareTo(reservation3) < 0);
    }
}
