package mma.admin.domains.interfaces.repository;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;

import java.util.List;
import java.util.Set;

public interface RoomsRepository {
    boolean exists(String id);
    boolean capacityRoomIsNotExceeded(String roomId, int capacity);
    List<Room> getRooms();
    Room getRoom(RoomId id);
    boolean allRoomsExist(List<Reservation> reservations);
}
