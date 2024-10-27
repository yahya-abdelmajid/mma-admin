package mma.admin.domains.interfaces.repository;

import mma.admin.domains.Room;

import java.util.Set;
import java.util.stream.Stream;

public interface RoomsRepository{
    void readAllRooms();
    Set<Room> getRooms();
    boolean exists(String id);
    boolean capacityRoomIsNotExceeded(String roomId, int capacity);
}
