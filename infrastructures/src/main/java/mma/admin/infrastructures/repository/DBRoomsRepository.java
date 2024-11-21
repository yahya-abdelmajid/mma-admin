package mma.admin.infrastructures.repository;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.RoomsReader;
import mma.admin.domains.interfaces.repository.RoomsRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DBRoomsRepository implements RoomsRepository {

    private List<Room> rooms;
    private Set<RoomId> roomIds;

    public DBRoomsRepository(RoomsReader roomsReader)
    {
        rooms = roomsReader.getRooms();
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
        return capacity<=rooms.get().getCapacity();
    }
}
