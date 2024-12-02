package mma.admin.domains;

import java.util.Objects;

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
     * @param roomId   Identifiant unique de la salle.
     * @param name     Nom de la salle.
     * @param capacity Capacité de la salle.
     */
    public Room {
        if (roomId == null) {
            throw new IllegalArgumentException("L'id de la salle ne peut pas être null.");
        }
        if (name == null || name.isBlank()||name.equals("")) {
            throw new IllegalArgumentException("Le nom ne peut pas être null ou vide.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("La capacité doit être un nombre positif.");
        }
    }

    /**
     * Vérifie si la capacité de la salle est suffisante pour le nombre de personnes attendues.
     * @param personExpected le nombre de personnes attendues
     * @return true si la capacité est suffisante, sinon false
     */
    public boolean isValid(int personExpected) {
        if (personExpected <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes attendues ne peut pas être négatif.");
        }

        return personExpected <= capacity&&personExpected >0;
    }

    /**
     * Retourne l'identifiant de la salle sous forme de chaîne.
     * @return l'identifiant de la salle.
     */
    public String getId() {
        return roomId.toString();
    }

    /**
     * Retourne une représentation textuelle de la salle.
     * @return une chaîne décrivant la salle.
     */
    @Override
    public String toString() {
        return String.format("Salle : %s (Cap. %d)\n", name, capacity);
    }
}
