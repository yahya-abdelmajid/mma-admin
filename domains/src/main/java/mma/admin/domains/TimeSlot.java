package mma.admin.domains;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Modèle représentant un créneau horaire.
 */
public class TimeSlot {
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Constructeur pour initialiser un créneau horaire avec une heure de début et de fin.
     *
     * @param start L'heure de début.
     * @param end L'heure de fin.
     */
    public TimeSlot(LocalDate day,LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.startTime = LocalDateTime.of(day, start);
        this.endTime = LocalDateTime.of(day, end);
    }

    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        this.startTime = start;
        this.endTime = end;
        this.day = start.toLocalDate();
        this.start = start.toLocalTime();
        this.end = end.toLocalTime();
    }

    public LocalTime getStart() {return start;}
    public LocalTime getEnd() {return end;}
    public LocalDate getDay() {return day;}
    public LocalDateTime getStartTime() {return startTime;}
    public LocalDateTime getEndTime() {return endTime;}

    // Vérifie que le créneau horaire est postérieur ou égal à l'instant présent
    public boolean isNotInPast() { return startTime != null && !startTime.isBefore(LocalDateTime.now());}

    // Méthode qui vérifie si l'heure d'un LocalDateTime est entre les heures en paramètre
    public boolean isWithinWorkingHours(LocalTime start, LocalTime end) {
        return !this.start.isBefore(start) && !this.start.isAfter(end)
                && !this.end.isBefore(start) && !this.end.isAfter(end);
    }


    public boolean isInTimeSlot(LocalTime time) {
        return time.isAfter(start) && time.isBefore(end)||
                time.equals(start)||time.equals(end);
    }

    @Override
    public String toString() {return day+" : "+start.toString() + " - " + end.toString();}

    //Compare de TimeSlot entre eux
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return start.equals(timeSlot.start) && end.equals(timeSlot.end)&&day.equals(timeSlot.day);
    }

    @Override
    public int hashCode() {return start.hashCode() + end.hashCode();}
}
