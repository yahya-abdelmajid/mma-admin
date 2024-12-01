package mma.admin.domains;

import java.util.Objects;

/**
 * Représente un identifiant unique pour une salle.
 *
 * @param id L'identifiant de la salle.
 */
public record RoomId(String id) {

    /**
     * Retourne une représentation textuelle de l'identifiant de la salle.
     *
     * @return L'identifiant sous forme de chaîne de caractères.
     */
    @Override
    public String toString() {
        return id;
    }
}





