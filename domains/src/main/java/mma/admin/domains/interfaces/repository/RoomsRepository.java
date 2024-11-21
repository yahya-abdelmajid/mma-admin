package mma.admin.domains.interfaces.repository;

import mma.admin.domains.Room;

import java.util.List;
import java.util.Set;

public interface RoomsRepository {
    boolean exists(String id);
    boolean capacityRoomIsNotExceeded(String roomId, int capacity);
    List<Room> getRooms();
}
