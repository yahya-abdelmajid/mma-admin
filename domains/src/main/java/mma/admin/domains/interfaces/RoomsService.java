package mma.admin.domains.interfaces;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;

import java.util.Set;
import java.util.stream.Stream;

public interface RoomsService {
    Set<RoomId> getAllIds();
    void readAllRooms();
    Set<Room> getRooms();
    boolean exists(String id);
    boolean capacityRoomIsNotExceeded(String roomId, int capacity);
    void addReservation(Reservation reservation);
    Reservation getReservation(Reservation reservation);
}
