package mma.admin.domains.interfaces.service;

import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.DataStorage;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<Room> getAllRooms();
}
