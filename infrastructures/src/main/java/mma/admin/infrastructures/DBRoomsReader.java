package mma.admin.infrastructures;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.RoomsReader;
import mma.admin.infrastructures.service.RoomService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DBRoomsReader implements RoomsReader {

    private RoomService roomService;
    private List<Room> rooms;

    public DBRoomsReader(RoomService roomService) {
        this.roomService = roomService;
        rooms = new ArrayList<>();
        readAllRooms();
    }

    @Override
    public Reservation getReservation(Reservation reservation) {
        return null;
    }

    public List<Room> getRooms(){
        return rooms;
    }

    @Override
    public void readAllRooms() {
      this.rooms=roomService.getAllRooms();
    }
}
