package mma.admin.infrastructures.csv;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.RoomsReader;
import mma.admin.domains.interfaces.repository.RoomsRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvRoomsReader {

    private String csvFile;
    private Set<Room> rooms;
    private Set<RoomId> roomIds;

    public CsvRoomsReader(String csvFile) {
        this.csvFile = csvFile;
        rooms = new LinkedHashSet<>();
        roomIds = new LinkedHashSet<>();
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    public boolean capacityRoomIsNotExceeded(String roomId, int capacity) {
        Optional<Room> rooms=this.rooms.stream().filter(room -> room.getId().equals(roomId)).findFirst();
        return capacity<=rooms.get().getCapacity();
    }

    /*public Reservation getReservation(Reservation reservation) {
        Reservation result = null;
        for(Room room : rooms) {
            System.out.println(room);
            result=room.getReservation(reservation);
            System.out.println(result);
            if(result!=null) return result;
        }
        return null;
    }*/



    /*@Override
    public void addReservation(Reservation r) {
        for (Room room : rooms) {
            if (room.getRoomId().equals(r.getRoomId())) {
                room.add(r);
            }
        }
    }*/


    public Set<RoomId> getAllIds() {
        return roomIds;
    }

    public boolean exists(String id){
        return rooms.stream().anyMatch(room -> room.getId().equals(id));
    }

    public Set<Room> getRooms(){
        return rooms;
    }


    public void readAllRooms() {
        String line = "";
        String csvSplitBy = ","; // Délimiteur (ici une virgule)

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Lire la première ligne pour ignorer l'en-tête
            br.readLine();

            // Lire chaque ligne du fichier
            while ((line = br.readLine()) != null) {
                // Utiliser la virgule pour séparer les colonnes
                String[] room = line.split(csvSplitBy);
                // Afficher les informations de chaque ligne
                String id = room[0];
                String name = room[1];
                int capacity = Integer.parseInt(room[2]);
                roomIds.add(new RoomId(id));
                rooms.add(new Room(id, name, capacity));
                System.out.println("Identifiant: " + id + ", Nom: " + name + ", Capacité: " + capacity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
