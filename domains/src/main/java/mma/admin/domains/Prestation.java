package mma.admin.domains;

/**
 * Représente une prestation avec un identifiant unique et une description.
 *
 * Cette classe est utilisée pour décrire des services ou des prestations
 * associés à une entité, comme une réservation ou un agenda.
 */
public class Prestation {

    /**
     * L'identifiant unique de la prestation.
     */
    private int id;

    /**
     * La description détaillée de la prestation.
     */
    private String description;

    /**
     * Constructeur avec paramètres.
     *
     * @param id          l'identifiant unique de la prestation
     * @param description la description de la prestation
     */
    public Prestation(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Constructeur par défaut.
     *
     * Ce constructeur est utile pour les cas où les valeurs des attributs
     * sont définies ultérieurement via des méthodes d'accès (getters et setters).
     */
    public Prestation() {}

    /**
     * Retourne l'identifiant de la prestation.
     *
     * @return l'identifiant unique de la prestation
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne la description de la prestation.
     *
     * @return la description de la prestation
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description de la prestation.
     *
     * @param description la nouvelle description de la prestation
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

