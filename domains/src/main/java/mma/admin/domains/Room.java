package mma.admin.domains;

import mma.admin.domains.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Modèle de la salle de réunion avec ses créneaux horaires disponibles.
 * Utilise un record pour simplifier la gestion des données immuables.
 *
 * @param roomId   Identifiant unique de la salle.
 * @param name     Nom de la salle.
 * @param capacity Capacité de la salle.
 */
public record Room(RoomId roomId, String name, int capacity) {

    /**
     * Constructeur principal pour initialiser une salle.
     *
     * @param roomId   Identifiant unique de la salle.
     * @param name     Nom de la salle.
     * @param capacity Capacité de la salle.
     */
    public Room {
        if (roomId == null) {
            throw new IllegalArgumentException("L'id de la salle ne peut pas être null.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être null.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("La capacité doit être positive.");
        }
    }

    /**
     * Retourne l'identifiant de la salle sous forme de chaîne.
     *
     * @return l'identifiant de la salle.
     */
    public String getId() {
        return roomId.toString();
    }

    /**
     * Retourne une représentation textuelle de la salle.
     *
     * @return une chaîne décrivant la salle.
     */
    @Override
    public String toString() {
        return "Salle : " + name + " (Cap. " + capacity + ")\n";
    }
}

