package mma.admin.domains;

import mma.admin.domains.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Modèle de la salle de réunion avec ses créneaux horaires disponibles.
 */
public class Room {
    private final RoomId roomId;
    private String name;
    private int capacity;

    /**
     * Constructeur pour initialiser une salle avec un nom et une liste de créneaux disponibles.
     *
     * @param name Le nom de la salle.
     * @param capacity Le nom
     */
    public Room(String roomId, String name, int capacity) {
        this.roomId = new RoomId(roomId);
        this.name = name;
        this.capacity = capacity;
    }

    /**
     * @return Le nom de la salle.
     */
    public String getName() {
        return name;
    }

    public RoomId getRoomId(){
        return roomId;
    }

    public String getId() {
        return roomId.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    @Override
    public String toString() {
        return "Room{id='" + roomId.id + "', name='" + name + "', capacity=" + capacity + "}";
    }
}
