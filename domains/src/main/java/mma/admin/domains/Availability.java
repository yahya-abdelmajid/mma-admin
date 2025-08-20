package mma.admin.domains;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Représente une disponibilité d'une salle avec un créneau horaire spécifique
 * et un score indiquant la pertinence de cette disponibilité.
 *
 * La classe implémente les interfaces {@link Comparable} et {@link Comparator}
 * pour permettre une comparaison basée sur le score.
 */
public class Availability implements Comparable<Availability>, Comparator<Availability> {

    /**
     * L'identifiant de la salle associée à cette disponibilité.
     */
    private final RoomId roomId;

    /**
     * Le créneau horaire associé à cette disponibilité.
     */
    private final TimeSlot ts;

    /**
     * Le score de la disponibilité, calculé en fonction de divers critères.
     */
    private int score;

    /**
     * Constructeur de la classe {@code Availability}.
     *
     * @param roomId   l'identifiant de la salle
     * @param timeSlot le créneau horaire de la disponibilité
     */
    public Availability(RoomId roomId, TimeSlot timeSlot) {
        this.roomId = roomId;
        this.ts = timeSlot;
        this.score = 0;
    }

    /**
     * Retourne l'identifiant de la salle associée à cette disponibilité.
     *
     * @return l'identifiant de la salle
     */
    public RoomId getRoom() {
        return roomId;
    }

    /**
     * Retourne le créneau horaire associé à cette disponibilité.
     *
     * @return le créneau horaire
     */
    public TimeSlot getTimeSlot() {
        return ts;
    }

    public int getScore() {
        return score;
    }

    /**
     * Calcule le score de la disponibilité en fonction de la date, de la durée,
     * et de la capacité.
     *
     * Le score est obtenu en combinant plusieurs critères :
     * - La proximité de la date de disponibilité avec la date actuelle.
     * - La durée du créneau horaire.
     * - L'adéquation entre la capacité de la salle et la capacité demandée.
     *
     * @param currentDate     la date de la disponibilité
     * @param room     la salle associée
     * @param capacity la capacité demandée
     */
    public void calculatorScore(LocalDate desiredDate,LocalDate currentDate, Room room, int capacity) {
        int scoreDate = 100 - Math.abs((int) ChronoUnit.DAYS.between(desiredDate, currentDate)) * 20;
        int scoreDuration = 100 - Math.abs((int) Duration.between(ts.getStart(), ts.getEnd()).toMinutes()) / 10;
        int scoreCapacity = 100 - Math.abs(room.capacity() - capacity);

        this.score = scoreDate + scoreDuration + scoreCapacity;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la disponibilité.
     *
     * @return une chaîne contenant l'identifiant de la salle et le créneau horaire
     */
    @Override
    public String toString() {
        return roomId + "" + ts;
    }

    /**
     * Compare cette disponibilité avec une autre en fonction de leur score.
     *
     * Les disponibilités ayant un score plus élevé sont considérées comme
     * prioritaires.
     *
     * @param o l'autre disponibilité à comparer
     * @return une valeur négative, zéro ou positive selon que cette disponibilité
     *         est respectivement supérieure, égale ou inférieure à l'autre
     */
    @Override
    public int compareTo(Availability o) {
        return Integer.compare(this.score,o.score);
    }

    /**
     * Compare deux disponibilités en fonction de leur score.
     *
     * @param a1 la première disponibilité à comparer
     * @param a2 la seconde disponibilité à comparer
     * @return une valeur négative, zéro ou positive selon que la première
     *         disponibilité a un score supérieur, égal ou inférieur à celui de la
     *         seconde
     */
    @Override
    public int compare(Availability a1, Availability a2) {
        return Integer.compare(a2.score, a1.score);
    }
}
