package mma.admin.infrastructures.repository;

import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.infrastructures.service.RoomService;

import java.util.List;
import java.util.Optional;

public class DBRoomsRepository implements RoomsRepository {

    private List<Room> rooms;

    public DBRoomsRepository(RoomService roomService)
    {
        rooms = roomService.getAllRooms();
    }

    public List<Room> getRooms(){
        return rooms;
    }

    @Override
    public boolean exists(String id){
        return rooms.stream().anyMatch(room -> room.getId().equals(id));
    }

    @Override
    public boolean capacityRoomIsNotExceeded(String roomId, int capacity) {
        Optional<Room> rooms=this.rooms.stream().filter(room -> room.getId().equals(roomId)).findFirst();
        return capacity<=rooms.get().capacity();
    }

    @Override
    public Room getRoom(RoomId id){
        return rooms.stream().filter(room -> room.roomId().equals(id)).findFirst().get();
    }
}
