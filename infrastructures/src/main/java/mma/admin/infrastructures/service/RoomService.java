package mma.admin.infrastructures.service;

import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.infrastructures.sql.SQLDataStorage;

import java.util.ArrayList;
import java.util.List;

public class RoomService {

    private final SQLDataStorage dataStorage;

    public RoomService(SQLDataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    public void getRoom(String id) {
        try{
            dataStorage.getRoomDao().findById(id);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms=new ArrayList<Room>();
        try{
            rooms=dataStorage.getRoomDao().findAll();
            return rooms;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
