package mma.admin.infrastructures.service;

import mma.admin.domains.Room;
import mma.admin.domains.interfaces.DataStorage;
import mma.admin.domains.interfaces.service.RoomService;

import java.util.List;

public class DBRoomService implements RoomService {

    private final DataStorage dataStorage;

    public DBRoomService(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    @Override
    public List<Room> getAllRooms() {return dataStorage.getRoomDao().findAll();}
}
