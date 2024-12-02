package mma.admin.domains;

/**
 * Représente une prestation avec un identifiant unique et une description.
 *
 * Cette classe (sous forme de record) est utilisée pour décrire des services ou des prestations
 * associés à une entité, comme une réservation ou un agenda.
 *
 * Les records en Java fournissent une structure immuable et concise pour des objets contenant
 * principalement des données.
 */
public record Prestation(
        /**
         * L'identifiant unique de la prestation.
         */
        int id,

        /**
         * La description détaillée de la prestation.
         */
        String description
) {
    /**
     * Constructeur principal.
     *
     * @param id          l'identifiant unique de la prestation
     * @param description la description de la prestation
     */
    public Prestation {
        // Validation ou transformation des paramètres, si nécessaire
        if (id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être un entier positif.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("La description ne peut pas être vide ou nulle.");
        }
    }

    /**
     * Méthode supplémentaire pour afficher les détails de la prestation.
     *
     * @return une chaîne contenant les détails de la prestation.
     */
    @Override
    public String toString() {
        return "Prestation{id=" + id + ", description='" + description + "'}";
    }
}
