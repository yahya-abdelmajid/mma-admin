package mma.admin.domains;

public class User {
    private String badge;
    private String fullName;
    private String email;

    // Constructeur
    public User(String badge, String fullName, String email) {
        this.badge = badge;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters
    public String getBadge() {
        return badge;
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
        return String.format("%s (%s, %s)", fullName, badge, email);
    }
}
