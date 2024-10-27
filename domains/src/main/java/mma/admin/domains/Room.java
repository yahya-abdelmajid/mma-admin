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
    private TreeSet<Reservation> reservations=new TreeSet<>();

    /**
     * Constructeur pour initialiser une salle avec un nom et une liste de créneaux disponibles.
     *
     * @param name Le nom de la salle.
     * @param capacity Le nom
     */
    public Room(RoomId roomId, String name, int capacity) {
        this.roomId = roomId;
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

    public TreeSet<Reservation> getReservations() {
        return reservations;
    }

    public void add(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getReservationsByDate(LocalDate date){
        return reservations.stream()
                .filter(reservation -> reservation.isDate(date))
                .collect(Collectors.toList());
    }

    public Reservation getReservation(Reservation reservation){
        System.out.println(reservations.getFirst().equals(reservation));
        System.out.println(reservations.getFirst().equals(reservation));
        return reservations.stream()
                .filter(r -> r.equals(reservation))
                .findFirst()
                .orElse(null); //permet de retourner null si aucune réservation ne correspond
    }

    public String getLigneMenu(LocalTime[] header,LocalDate date,String signe){
        StringBuilder ligne= new StringBuilder(String.format("%-10s", getId()));
        List<Reservation> reservations = getReservationsByDate(date);

        int isNotInTimeSlot = 0;
        for (int i=0;i<header.length;i++) {
            for (Reservation reservation : reservations) {
                if (reservation.isInTimeSlot(header[i])) {
                    ligne.append(String.format("%-10s", signe));
                    isNotInTimeSlot++;
                    break;
                }
            }
            if (isNotInTimeSlot == i) {
                ligne.append(String.format("%-10s", ' '));
                isNotInTimeSlot++;
            }
        }
        return ligne.toString();
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
