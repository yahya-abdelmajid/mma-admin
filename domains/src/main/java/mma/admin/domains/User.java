package mma.admin.domains;

public class User {
    private String matricule;
    private String fullName;
    private String email;

    // Constructeur
    public User(String matricule, String fullName, String email) {
        this.matricule = matricule;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters
    public String getMatricule() {
        return matricule;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return String.format("%s (%s, %s)", fullName, matricule, email);
    }
}
