package mma.admin.domains;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Modèle de réservation de salle avec les détails de la réservation.
 */
public class Reservation implements Comparable<Reservation> {
    private RoomId roomId;
    private String personId;
    private LocalDateTime date;
    private TimeSlot timeSlot;
    private String description;

    /**
     * Constructeur pour initialiser une réservation avec tous ses détails.
     *
     * @param roomId Le nom de la salle réservée.
     * @param personId Le matricule de la personne qui réserve.
     * @param dateTime La date de la réservation.
     * @param timeSlot La date et l'heure de début et de fin
     * @param description La description de la réservation.
     */
    public Reservation(RoomId roomId, String personId, LocalDateTime dateTime, TimeSlot timeSlot, String description) {
        this.roomId = roomId;
        this.personId = personId;
        this.date = dateTime;
        this.description = description;
        this.timeSlot = timeSlot;
    }

    // Getters pour les attributs...
    public RoomId getRoomId() { return roomId; }
    public String getPersonId() { return personId; }
    public LocalDateTime getDate() { return date; }
    public String getDescription() { return description; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public void setRoomId(RoomId roomId) { this.roomId = roomId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setTimeSlot(TimeSlot timeSlot) { this.timeSlot = timeSlot; }
    public void setDescription(String description) { this.description = description; }

    public boolean isDate(LocalDate date) {
        return date.equals(this.timeSlot.getDay());
    }

    public boolean isInTimeSlot(LocalTime time) {
        return timeSlot.isInTimeSlot(time);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "roomId=" + roomId +
                ", personId='" + personId + '\'' +
                ", date=" + date +
                ", timeSlot=" + timeSlot +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Compare 2 objets réservation classe généré par une IA
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(roomId, that.roomId) &&
                Objects.equals(timeSlot, that.timeSlot);
    }

    // Redéfinition de hashCode généré par une IA
    @Override
    public int hashCode() {
        return Objects.hash(roomId, personId, date, timeSlot);
    }

    @Override
    public int compareTo(Reservation r2) {
        // Comparer les dates de début
        int startComparison = getTimeSlot().getStartTime().compareTo(r2.getTimeSlot().getStartTime())
                +getRoomId().getId().compareTo(r2.getRoomId().getId());
        if (startComparison != 0) return startComparison;
        // Si les dates de début sont égales, comparer les dates de fin
        return getTimeSlot().getEndTime().compareTo(r2.getTimeSlot().getEndTime());
    }
}
