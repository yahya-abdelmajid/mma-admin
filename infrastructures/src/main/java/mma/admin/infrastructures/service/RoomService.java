package mma.admin.infrastructures.service;

import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.DataStorage;
import mma.admin.infrastructures.sql.SQLDataStorage;

import java.util.ArrayList;
import java.util.List;

public class RoomService {

    private final DataStorage dataStorage;

    public RoomService(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    public Room getRoom(String id) {return dataStorage.getRoomDao().findById(id);}

    public List<Room> getAllRooms() {return dataStorage.getRoomDao().findAll();}
}
