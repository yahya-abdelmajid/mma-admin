package mma.admin.domains;

import mma.admin.domains.exceptions.NoPositiveCapacityException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Représente une réservation effectuée pour une salle à une date et un créneau horaire spécifiques.
 *
 * Cette classe contient des informations telles que l'identifiant de la salle, l'utilisateur ayant
 * effectué la réservation, la description, et une liste de prestations associées.
 */
public class Reservation implements Comparable<Reservation> {


    private final int id;
    private final RoomId roomId;
    private final String personId;
    private final TimeSlot timeSlot;
    private final String description;
    private final List<Prestation> prestations;
    private final int expectedPerson;

    /**
     * Constructeur permettant de créer une réservation avec tous les détails.
     *
     * @param id          l'identifiant unique de la réservation
     * @param roomId      le nom de la salle réservée
     * @param personId    le matricule de la personne ayant effectué la réservation
     * @param timeSlot    le créneau horaire réservé
     * @param description une description de la réservation
     */
    public Reservation(int id, String roomId, String personId, TimeSlot timeSlot, String description,int expectedPerson) {
        this.id = id;
        this.roomId = new RoomId(requireNonBlank(roomId, "roomId"));
        this.personId = requireNonBlank(personId, "personId");
        this.timeSlot = java.util.Objects.requireNonNull(timeSlot, "timeSlot ne peut pas être nul.");
        this.description = normalizeDescription(description,500);
        this.expectedPerson = requirePositive(expectedPerson);
        this.prestations = new ArrayList<>();
    }

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

    public int getExpectedPerson() {
        return expectedPerson;
    }

    /**
     * Retourne la liste des prestations associées à cette réservation.
     *
     * @return la liste des prestations
     */
    public List<Prestation> getPrestations() {
        return Collections.unmodifiableList(prestations);
    }

    /**
     * Ajoute une prestation à la liste des prestations associées.
     *
     * @param prestation la prestation à ajouter
     */
    public void addPrestation(Prestation prestation) {
        if (prestation == null) {
            throw new IllegalArgumentException("La prestation ajoutée ne peut pas être nulle.");
        }
        if (prestations.contains(prestation)) {
            throw new IllegalArgumentException("Cette prestation est déjà ajoutée.");
        }
        prestations.add(prestation);
    }

    /**
     * Vérifie si un créneau horaire donné chevauche le créneau de cette réservation.
     *
     * @param timeSlot le créneau horaire à vérifier
     * @return {@code true} si les créneaux se chevauchent, {@code false} sinon
     */
    boolean isOverlay(TimeSlot timeSlot) {
        if (timeSlot == null) {
            throw new IllegalArgumentException("Le créneau horaire fourni ne peut pas être nul.");
        }
        return this.timeSlot.isOverlay(timeSlot);
    }

    /**
     * Vérifie si la réservation a lieu à une date donnée.
     *
     * @param date la date à vérifier
     * @return {@code true} si la réservation a lieu à cette date, {@code false} sinon
     */
    public boolean isDate(LocalDate date) {
        return date != null && date.equals(this.timeSlot.getDay());
    }

    /**
     * Vérifie si une heure donnée est incluse dans le créneau horaire de la réservation.
     *
     * @param time l'heure à vérifier
     * @return {@code true} si l'heure est incluse, {@code false} sinon
     */
    boolean isInTimeSlot(LocalTime time) {
        return time != null && timeSlot.isInTimeSlot(time);
    }

    /**
     * Retourne une chaîne décrivant les prestations associées à cette réservation.
     *
     * @return une chaîne contenant la description des prestations
     */
    public String prestationsToString() {
        if (prestations.isEmpty()) {
            return "Aucun service prévu.";
        }
        StringBuilder result = new StringBuilder("Services prévus : ");
        for (Prestation p : prestations) {
            result.append(p.description()).append(", ");
        }
        return result.substring(0, result.length() - 2); // Supprime la virgule et l'espace finaux
    }

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

    boolean isBeetweenDate(LocalDate startDate, LocalDate endDate) {
        return timeSlot.isBeetween(startDate, endDate);
    }

    /**
     * Retourne le code de hachage de la réservation.
     *
     * @return le code de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hash(roomId, timeSlot);
    }

    /**
     * Compare deux réservations en fonction de leurs créneaux horaires et identifiants de salle.
     *
     * @param r2 l'autre réservation à comparer
     * @return un entier négatif, zéro ou positif selon l'ordre des réservations
     */
    @Override
    public int compareTo(Reservation r2) {
        if (r2 == null) {
            throw new IllegalArgumentException("Impossible de comparer avec une réservation nulle.");
        }
        int startComparison = getTimeSlot().getStartTime().compareTo(r2.getTimeSlot().getStartTime());
        if (startComparison != 0) {return startComparison;}
        return getRoomId().id().compareTo(r2.getRoomId().id());
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " ne peut pas être nul ou vide.");
        }
        return value;
    }

    private int requirePositive(int value) {
        if (value <= 0) {
            throw new NoPositiveCapacityException("La capacité doit être strictement positif.");
        }
        return value;
    }

    private static String normalizeDescription(String desc,int lengthMax) {
        if (desc == null || desc.isBlank()){ return null;} // ou "" selon ton choix
        if (desc.length() > lengthMax) {
            throw new IllegalArgumentException("La description ne peut pas dépasser 500 caractères.");
        }
        return desc;
    }
}
