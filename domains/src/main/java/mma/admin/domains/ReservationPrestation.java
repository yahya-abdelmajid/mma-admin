package mma.admin.domains;

/**
 * Représentation d'une association entre une réservation et une prestation.
 *
 * @param reservationId L'identifiant unique de la réservation.
 * @param serviceId     L'identifiant unique de la prestation.
 */
public record ReservationPrestation(int reservationId, int serviceId) {

    /**
     * Constructeur principal pour initialiser une association entre une réservation et une prestation.
     *
     * @param reservationId L'identifiant unique de la réservation.
     * @param serviceId     L'identifiant unique de la prestation.
     */
    public ReservationPrestation {
        if (reservationId <= 0) {
            throw new IllegalArgumentException("L'identifiant de la réservation doit être positif.");
        }
        if (serviceId <= 0) {
            throw new IllegalArgumentException("L'identifiant de la prestation doit être positif.");
        }
    }

    /**
     * Retourne une représentation textuelle de l'association.
     *
     * @return Une chaîne représentant l'association entre réservation et prestation.
     */
    @Override
    public String toString() {
        return "ReservationPrestation{" +
                "reservationId=" + reservationId +
                ", serviceId=" + serviceId +
                '}';
    }
}
