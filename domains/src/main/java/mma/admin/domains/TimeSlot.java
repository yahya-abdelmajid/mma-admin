package mma.admin.domains;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Modèle représentant un créneau horaire.
 */
public class TimeSlot implements Comparable<TimeSlot> {

    private final LocalDate day;
    private final LocalTime start;
    private final LocalTime end;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Constructeur pour initialiser un créneau horaire avec une date, une heure de début et une heure de fin.
     *
     * @param day   Le jour du créneau horaire.
     * @param start L'heure de début.
     * @param end   L'heure de fin.
     */
    public TimeSlot(LocalDate day, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.startTime = LocalDateTime.of(day, start);
        this.endTime = LocalDateTime.of(day, end);
    }

    /**
     * Constructeur pour initialiser un créneau horaire avec des instants de début et de fin.
     *
     * @param start L'instant de début (date et heure).
     * @param end   L'instant de fin (date et heure).
     */
    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        this.startTime = start;
        this.endTime = end;
        this.day = start.toLocalDate();
        this.start = start.toLocalTime();
        this.end = end.toLocalTime();
    }

    /**
     * Constructeur pour initialiser un créneau horaire avec l'heure de début et de fin pour le jour actuel.
     *
     * @param start L'heure de début.
     * @param end   L'heure de fin.
     */
    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
        this.day = LocalDate.now();
        this.startTime = LocalDateTime.of(day, start);
        this.endTime = LocalDateTime.of(day, end);
      }

    /**
     * Obtient l'heure de début du créneau.
     *
     * @return L'heure de début.
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * Obtient l'heure de fin du créneau.
     *
     * @return L'heure de fin.
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * Obtient le jour du créneau.
     *
     * @return La date du créneau.
     */
    public LocalDate getDay() {
        return day;
    }

    /**
     * Obtient l'instant de début du créneau.
     *
     * @return L'instant de début (date et heure).
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Obtient l'instant de fin du créneau.
     *
     * @return L'instant de fin (date et heure).
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Vérifie que le créneau horaire est postérieur ou égal à l'instant présent.
     *
     * @return {@code true} si le créneau n'est pas dans le passé, {@code false} sinon.
     */
    public boolean isNotInPast() {
        return startTime != null && !startTime.isBefore(LocalDateTime.now());
    }

    /**
     * Vérifie si le créneau actuel est compris dans les heures de travail spécifiées.
     *
     * @param slot Le créneau horaire à comparer.
     * @return {@code true} si le créneau est dans les heures de travail, {@code false} sinon.
     */
    public boolean isWithinWorkingHours(TimeSlot slot) {

        return (this.start.isAfter(slot.getStart()) ||this.start.equals(slot.getStart()))
                && this.end.isBefore(slot.getEnd()) || this.end.equals(slot.getEnd());
    }

    /**
     * Vérifie si deux créneaux horaires se chevauchent.
     *
     * @param ts Le créneau horaire à comparer.
     * @return {@code true} si les créneaux se chevauchent, {@code false} sinon.
     */
    public boolean isOverlay(TimeSlot ts) {
        return this.start.isBefore(ts.getEnd()) && ts.getStart().isBefore(this.end);
    }

    /**
     * Vérifie si une heure donnée est incluse dans le créneau horaire.
     *
     * @param time L'heure à vérifier.
     * @return {@code true} si l'heure est incluse, {@code false} sinon.
     */
    public boolean isInTimeSlot(LocalTime time) {
        return (this.start.isBefore(time) && time.isBefore(this.end))||
                this.start.equals(time) || time.equals(this.end);
    }

    /**
     * Représentation textuelle du créneau horaire.
     *
     * @return Une chaîne de caractères représentant le jour et l'intervalle de temps.
     */
    @Override
    public String toString() {
        return day + " : " + start.toString() + " - " + end.toString();
    }

    /**
     * Compare deux créneaux horaires pour l'égalité.
     *
     * @param o L'objet à comparer.
     * @return {@code true} si les créneaux sont égaux, {@code false} sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) o;
        return start.equals(timeSlot.start) && end.equals(timeSlot.end) && day.equals(timeSlot.day);
    }

    /**
     * Génère le code de hachage du créneau horaire.
     *
     * @return Le code de hachage.
     */
    @Override
    public int hashCode() {
        return start.hashCode() + end.hashCode();
    }

    /**
     * Compare deux créneaux horaires selon leur heure de début et de fin.
     *
     * @param ts Le créneau horaire à comparer.
     * @return Un entier négatif, zéro ou positif selon l'ordre relatif des créneaux.
     */
    @Override
    public int compareTo(TimeSlot ts) {
        int startComparison = startTime.compareTo(ts.getStartTime()) + endTime.compareTo(ts.getEndTime());
        if (day != null && ts.getDay() != null) {
            startComparison += day.compareTo(ts.getDay());
        }
        return startComparison;
    }
}
