package mma.admin.domains;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Agenda {
    private final RoomId roomId;
    private TreeSet<Reservation> reservations;

    public Agenda(RoomId roomId) {
        this.roomId = roomId;
        this.reservations = new TreeSet<>();
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

    public TreeSet<Reservation> getReservations() {
        return reservations;
    }

    public String getLigneMenu(LocalTime[] header,LocalDate date,String signe){
        StringBuilder ligne= new StringBuilder(String.format("%-10s", roomId.getId()));
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
}
