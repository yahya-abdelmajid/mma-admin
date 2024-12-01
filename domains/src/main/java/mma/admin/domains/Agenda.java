package mma.admin.domains;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Représente un agenda pour une salle donnée, permettant de gérer les réservations
 * et les disponibilités.
 *
 * Cette classe fournit des méthodes pour ajouter des réservations, rechercher
 * des créneaux disponibles, et consulter les réservations associées à une salle.
 * Les commentaires ont étaient généré via une IA
 */
public class Agenda {

    /**
     * La salle associée à cet agenda.
     */
    private final Room room;

    /**
     * L'ensemble des réservations associées à cet agenda.
     */
    private final Set<Reservation> reservations;

    /**
     * L'ensemble des disponibilités calculées pour cet agenda.
     */
    private final Set<Availability> availabilities;

    /**
     * Crée un nouvel agenda pour une salle donnée.
     *
     * @param room la salle associée à cet agenda
     */
    public Agenda(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        this.room = room;
        this.reservations = new TreeSet<>();
        this.availabilities = new TreeSet<>();
    }

    /**
     * Ajoute une réservation à l'agenda.
     *
     * @param reservation la réservation à ajouter
     */
    public void add(Reservation reservation) {
        reservations.add(reservation);
    }

    /**
     * Retourne l'ensemble des disponibilités calculées pour cet agenda.
     *
     * @return un ensemble de {@link Availability} représentant les disponibilités
     */
    public Set<Availability> getAvailabilities() {
        return availabilities;
    }

    /**
     * Récupère la liste des réservations correspondant à une date spécifique.
     *
     * @param date la date des réservations à rechercher
     * @return une liste de {@link Reservation} correspondant à la date donnée
     */
    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservations.stream()
                .filter(reservation -> reservation.isDate(date))
                .collect(Collectors.toList());
    }

    /**
     * Retourne l'ensemble des réservations de cet agenda.
     *
     * @return un ensemble de {@link Reservation}
     */
    public Set<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Retourne la salle associée à cet agenda.
     *
     * @return la salle associée
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Génère une ligne de menu représentant l'occupation des créneaux horaires pour
     * une date donnée.
     *
     * @param header un tableau de créneaux horaires
     * @param date   la date pour laquelle générer la ligne
     * @param signe  le symbole représentant une occupation
     * @return une chaîne formatée représentant l'occupation
     */
    public String getLigneMenu(LocalTime[] header, LocalDate date, String signe) {
        StringBuilder ligne = new StringBuilder(String.format("%-10s", room.getId()));
        List<Reservation> reservations = getReservationsByDate(date);

        for (LocalTime timeSlot : header) {
            ligne.append(getTimeSlotSymbol(reservations, timeSlot, signe));
        }

        return ligne.toString();
    }

    /**
     * Détermine le symbole à utiliser pour un créneau horaire spécifique.
     *
     * @param reservations les réservations existantes
     * @param timeSlot     le créneau horaire à examiner
     * @param signe        le symbole représentant une occupation
     * @return le symbole formaté pour ce créneau
     */
    private String getTimeSlotSymbol(List<Reservation> reservations, LocalTime timeSlot, String signe) {
        for (Reservation reservation : reservations) {
            if (reservation.isInTimeSlot(timeSlot)) return String.format("%-10s", signe);
        }
        return String.format("%-10s", ' ');
    }

    /**
     * Recherche les disponibilités pour une plage horaire spécifique, une date,
     * une durée, et une capacité minimale.
     *
     * @param wh       la plage horaire à examiner
     * @param date     la date des disponibilités
     * @param duration la durée minimale des disponibilités
     * @param capacity la capacité minimale requise
     */
    public void searchAvailabilities(TimeSlot wh, LocalDate date, Duration duration, int capacity) {
        Set<Reservation> reservations = new TreeSet<>(getReservationsByDate(date));

        if (reservations.isEmpty()) {
            addAvailability(new TimeSlot(date, wh.getStart(), wh.getEnd()), capacity, duration);
        } else {
            calculateAvailabilities(date, wh, duration, capacity, reservations);
        }
    }

    /**
     * Calcule les disponibilités à partir des réservations existantes.
     *
     * @param date         la date des disponibilités
     * @param wh           la plage horaire à examiner
     * @param duration     la durée minimale des disponibilités
     * @param capacity     la capacité minimale requise
     * @param reservations les réservations existantes
     */
    private void calculateAvailabilities(LocalDate date, TimeSlot wh, Duration duration, int capacity, Set<Reservation> reservations) {
        LocalTime start = wh.getStart();
        LocalTime end = wh.getEnd();
        LocalTime temp = start;
        Duration range = Duration.ZERO;

        while (!temp.isAfter(end)) {
            TimeSlot timeSlot = new TimeSlot(start, temp.plusMinutes(1));
            range = range.plusMinutes(30);
            temp = start.plus(range);

            for (Reservation r : reservations) {
                boolean isOverlay = r.isOverlay(timeSlot);

                if ((isOverlay || temp.isAfter(end)) && range.compareTo(duration) >= 0) {
                    addAvailability(new TimeSlot(date, start, temp.minusMinutes(30)), capacity, duration);
                    range = Duration.ZERO;
                    break;
                } else if (isOverlay) {
                    start = r.getTimeSlot().getEnd();
                    range = Duration.ZERO;
                    temp = start;
                    break;
                }
            }
        }
    }

    /**
     * Ajoute une disponibilité à l'ensemble des disponibilités.
     *
     * @param timeSlot la plage horaire de la disponibilité
     * @param capacity la capacité minimale requise
     * @param duration la durée de la disponibilité
     */
    private void addAvailability(TimeSlot timeSlot, int capacity, Duration duration) {
        Availability availability = new Availability(room.roomId(), timeSlot);
        availability.calculatorScore(timeSlot.getDay(), room, duration, capacity);
        availabilities.add(availability);
    }
}

