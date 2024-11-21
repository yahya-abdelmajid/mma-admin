package mma.admin.domains.interfaces;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;

import java.util.List;
import java.util.Set;

public interface RoomsReader {
    void readAllRooms();
    List<Room> getRooms();
    Reservation getReservation(Reservation reservation);
}
