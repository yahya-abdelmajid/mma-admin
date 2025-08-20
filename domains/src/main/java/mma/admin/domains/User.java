package mma.admin.domains;

/**
 * Représente un utilisateur avec un badge, un nom complet et une adresse e-mail.
 *
 * @param badge   Le badge unique de l'utilisateur.
 * @param fullName Le nom complet de l'utilisateur.
 * @param email   L'adresse e-mail de l'utilisateur.
 */
public record User(String badge, String fullName, String email) {

    /**
     * Constructeur pour initialiser un utilisateur.
     *
     * @param badge   Le badge unique de l'utilisateur.
     * @param fullName Le nom complet de l'utilisateur.
     * @param email   L'adresse e-mail de l'utilisateur.
     * @throws IllegalArgumentException si l'un des paramètres est null ou vide.
     */
    public User {
        if (badge == null || badge.isBlank()) {
            throw new IllegalArgumentException("Le badge ne peut pas être null ou vide.");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Le nom complet ne peut pas être null ou vide.");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("L'adresse e-mail est invalide.");
        }
    }

    /**
     * Retourne une représentation textuelle de l'utilisateur.
     *
     * @return Une chaîne de caractères contenant le nom complet, le badge et l'adresse e-mail.
     */
    @Override
    public String toString() {
        return String.format("Responsable : %s (%s, %s)\n", fullName, badge, email);
    }
}

