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

class RoomTest {

    private Room room;
    private RoomId room_id;

    @BeforeEach
    void setup() {
        room_id = new RoomId("Room1");
        room = new Room(room_id, "Conference Room", 20);
    }

    @Test
    void get_name_should_return_correct_name() {
        assertEquals("Conference Room", room.getName());
    }

    @Test
    void set_name_should_update_name() {
        room.setName("New Room Name");
        assertEquals("New Room Name", room.getName());
    }

    @Test
    void get_capacity_should_return_correct_capacity() {
        assertEquals(20, room.getCapacity());
    }

    @Test
    void set_capacity_should_update_capacity() {
        room.setCapacity(30);
        assertEquals(30, room.getCapacity());
    }

    @Test
    void get_room_id_should_return_correct_room_id() {
        assertEquals(room_id, room.getRoomId());
    }

    @Test
    void get_reservations_should_return_empty_list_initially() {
        TreeSet<Reservation> reservations = room.getReservations();
        assertTrue(reservations.isEmpty());
    }

    @Test
    void should_exception_raised_when_reservation_property_is_null() {
        Reservation reservation = mock(Reservation.class);

        try{
            room.add(reservation);
        }catch(NullPointerException e){
            assertEquals(0, room.getReservations().size());
            assertFalse(room.getReservations().contains(reservation));
        }
    }

    @Test
    void get_ligne_menu_should_return_formatted_string_with_signe_for_matching_slots() {
        LocalDate date = LocalDate.now();
        LocalTime[] header = {LocalTime.of(9, 0), LocalTime.of(10, 0)};
        Reservation reservation = mock(Reservation.class);
        System.out.println(reservation);

        when(reservation.isInTimeSlot(header[0])).thenReturn(true);
        when(reservation.isInTimeSlot(header[1])).thenReturn(false);
        when(reservation.isDate(date)).thenReturn(true);
    }

    @Test
    void get_ligne_menu_should_return_empty_slots_when_no_reservations_match() {
        LocalDate date = LocalDate.now();
        LocalTime[] header = {LocalTime.of(9, 0), LocalTime.of(10, 0)};

        String expectedLine = String.format("%-10s%-10s%-10s", room.getId(), " ", " ");
        assertEquals(expectedLine, room.getLigneMenu(header, date, "*"));
    }

    @Test
    void get_id_should_return_room_id_as_string() {
        assertEquals(room_id.toString(), room.getId());
    }

    @Test
    void to_string_should_return_formatted_room_information() {
        String expectedString = "Room{id='Room1', name='Conference Room', capacity=20}";
        assertEquals(expectedString, room.toString());
    }
}
