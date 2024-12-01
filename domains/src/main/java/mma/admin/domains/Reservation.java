package mma.admin.domains;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente une réservation effectuée pour une salle à une date et un créneau horaire spécifiques.
 *
 * Cette classe contient des informations telles que l'identifiant de la salle, l'utilisateur ayant
 * effectué la réservation, la description, et une liste de prestations associées.
 */
public class Reservation implements Comparable<Reservation> {

    /**
     * Identifiant unique de la réservation.
     */
    private final int id;

    /**
     * Identifiant de la salle associée à cette réservation.
     */
    private final RoomId roomId;

    /**
     * Identifiant de la personne ayant effectué la réservation.
     */
    private final String personId;

    /**
     * Date et heure de la réservation.
     */
    private final LocalDateTime date;

    /**
     * Créneau horaire (début et fin) de la réservation.
     */
    private TimeSlot timeSlot;

    /**
     * Description de la réservation.
     */
    private String description;

    /**
     * Liste des prestations associées à cette réservation.
     */
    private List<Prestation> prestations;

    /**
     * Constructeur permettant de créer une réservation avec tous les détails.
     *
     * @param id          l'identifiant unique de la réservation
     * @param roomId      le nom de la salle réservée
     * @param personId    le matricule de la personne ayant effectué la réservation
     * @param dateTime    la date et l'heure de la réservation
     * @param timeSlot    le créneau horaire réservé
     * @param description une description de la réservation
     */
    public Reservation(int id, String roomId, String personId, LocalDateTime dateTime, TimeSlot timeSlot, String description) {
        this.id = id;
        this.roomId = new RoomId(roomId);
        this.personId = personId;
        this.date = dateTime;
        this.description = description;
        this.timeSlot = timeSlot;
        this.prestations = new ArrayList<>();
    }

    // Getters

    /**
     * Retourne l'identifiant unique de la réservation.
     *
     * @return l'identifiant de la réservation
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne l'identifiant de la salle associée à cette réservation.
     *
     * @return l'identifiant de la salle
     */
    public RoomId getRoomId() {
        return roomId;
    }

    /**
     * Retourne l'identifiant de la personne ayant effectué la réservation.
     *
     * @return l'identifiant de la personne
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Retourne la date et l'heure de la réservation.
     *
     * @return la date et l'heure
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Retourne la description de la réservation.
     *
     * @return la description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retourne le créneau horaire de la réservation.
     *
     * @return le créneau horaire
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Retourne la liste des prestations associées à cette réservation.
     *
     * @return la liste des prestations
     */
    public List<Prestation> getPrestations() {
        return prestations;
    }

    // Setters

    /**
     * Modifie le créneau horaire de la réservation.
     *
     * @param timeSlot le nouveau créneau horaire
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    /**
     * Modifie la description de la réservation.
     *
     * @param description la nouvelle description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Modifie la liste des prestations associées à cette réservation.
     *
     * @param prestations la nouvelle liste de prestations
     */
    public void setPrestations(List<Prestation> prestations) {
        this.prestations = prestations;
    }

    // Méthodes spécifiques

    /**
     * Ajoute une prestation à la liste des prestations associées.
     *
     * @param prestation la prestation à ajouter
     */
    public void addPrestation(Prestation prestation) {
        prestations.add(prestation);
    }

    /**
     * Vérifie si un créneau horaire donné chevauche le créneau de cette réservation.
     *
     * @param timeSlot le créneau horaire à vérifier
     * @return {@code true} si les créneaux se chevauchent, {@code false} sinon
     */
    public boolean isOverlay(TimeSlot timeSlot) {
        return this.timeSlot.isOverlay(timeSlot);
    }

    /**
     * Vérifie si la réservation a lieu à une date donnée.
     *
     * @param date la date à vérifier
     * @return {@code true} si la réservation a lieu à cette date, {@code false} sinon
     */
    public boolean isDate(LocalDate date) {
        return date.equals(this.timeSlot.getDay());
    }

    /**
     * Vérifie si une heure donnée est incluse dans le créneau horaire de la réservation.
     *
     * @param time l'heure à vérifier
     * @return {@code true} si l'heure est incluse, {@code false} sinon
     */
    public boolean isInTimeSlot(LocalTime time) {
        return timeSlot.isInTimeSlot(time);
    }

    /**
     * Retourne une chaîne décrivant les prestations associées à cette réservation.
     *
     * @return une chaîne contenant la description des prestations
     */
    public String prestationsToString() {
        StringBuilder result = new StringBuilder("Services prévus : ");
        for (Prestation p : prestations) {
            result.append(p.getDescription()).append(", ");
        }
        return result.toString();
    }

    // Méthodes redéfinies

    /**
     * Retourne une représentation textuelle de la réservation.
     *
     * @return une chaîne décrivant la réservation
     */
    @Override
    public String toString() {
        return String.format("%s, de %s à %s\nDescription : %s\n",
                timeSlot.getDay(), timeSlot.getStart(), timeSlot.getEnd(), description);
    }

    /**
     * Vérifie l'égalité entre deux réservations en se basant sur la salle et le créneau horaire.
     *
     * @param o l'objet à comparer
     * @return {@code true} si les réservations sont égales, {@code false} sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Reservation that = (Reservation) o;
        return Objects.equals(roomId, that.roomId) &&
                Objects.equals(timeSlot, that.timeSlot);
    }

    /**
     * Retourne le code de hachage de la réservation.
     *
     * @return le code de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hash(roomId, personId, date, timeSlot);
    }

    /**
     * Compare deux réservations en fonction de leurs créneaux horaires et identifiants de salle.
     *
     * @param r2 l'autre réservation à comparer
     * @return un entier négatif, zéro ou positif selon l'ordre des réservations
     */
    @Override
    public int compareTo(Reservation r2) {
        int startComparison = getTimeSlot().getStartTime().compareTo(r2.getTimeSlot().getStartTime())
                + getRoomId().id().compareTo(r2.getRoomId().id());
        if (startComparison != 0) {return startComparison;}
        return getTimeSlot().getEndTime().compareTo(r2.getTimeSlot().getEndTime());
    }
}

