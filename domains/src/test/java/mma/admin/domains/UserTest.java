package mma.admin.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");
    }

    // Vérifie l'affichage d'un user
    @Test
    void should_return_string_format_when_display_user() {
        String expected = "Jean Dupont (A123456, j.dupont@helmo.be)";
        assertEquals(expected, user.toString());
    }

    // Vérifie les appels aux getters
    @Test
    void should_return_value_of_getters_when_there_are_calling() {
        assertEquals("Jean Dupont", user.fullName());
        assertEquals("j.dupont@helmo.be", user.email());
        assertEquals("A123456", user.badge());
    }

    // Vérifie l'égalité de deux utilisateurs identiques
    @Test
    void should_return_true_when_users_are_equal() {
        User identicalUser = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");
        assertEquals(user, identicalUser);
    }

    // Vérifie que deux utilisateurs avec des badges différents ne sont pas égaux
    @Test
    void should_return_false_when_users_have_different_badges() {
        User differentBadgeUser = new User("B654321", "Jean Dupont", "j.dupont@helmo.be");
        assertNotEquals(user, differentBadgeUser);
    }

    // Vérifie que deux utilisateurs avec des emails différents ne sont pas égaux
    @Test
    void should_return_false_when_users_have_different_emails() {
        User differentEmailUser = new User("A123456", "Jean Dupont", "jean.dupont@autre.be");
        assertNotEquals(user, differentEmailUser);
    }

    // Vérifie que deux utilisateurs avec des noms différents ne sont pas égaux
    @Test
    void should_return_false_when_users_have_different_names() {
        User differentNameUser = new User("A123456", "John Doe", "j.dupont@helmo.be");
        assertNotEquals(user, differentNameUser);
    }

    // Vérifie que deux utilisateurs identiques ont le même hashCode
    @Test
    void should_return_same_hash_code_for_identical_users() {
        User identicalUser = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");
        assertEquals(user.hashCode(), identicalUser.hashCode());
    }

    // Vérifie que deux utilisateurs différents ont des hashCodes différents
    @Test
    void should_return_different_hash_code_for_different_users() {
        User differentUser = new User("B654321", "Jean Dupont", "j.dupont@helmo.be");
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }

    // Vérifie qu'une exception est levée lorsque le badge est null
    @Test
    void should_throw_exception_when_badge_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "Jean Dupont", "j.dupont@helmo.be"));
    }

    // Vérifie qu'une exception est levée lorsque le nom complet est null
    @Test
    void should_throw_exception_when_full_name_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", null, "j.dupont@helmo.be"));
    }

    // Vérifie qu'une exception est levée lorsque l'email est null
    @Test
    void should_throw_exception_when_email_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "Jean Dupont", null));
    }

    // Vérifie qu'une exception est levée lorsque l'email est invalide
    @Test
    void should_throw_exception_when_email_is_invalid() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "Jean Dupont", "invalid-email"));
    }

    // Vérifie qu'une exception est levée lorsque le badge est vide
    @Test
    void should_throw_exception_when_badge_is_empty() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "Jean Dupont", "j.dupont@helmo.be"));
    }

    // Vérifie qu'une exception est levée lorsque le nom est vide
    @Test
    void should_throw_exception_when_full_name_is_empty() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "", "j.dupont@helmo.be"));
    }

    // Vérifie qu'une exception est levée lorsque l'email est vide
    @Test
    void should_throw_exception_when_email_is_empty() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "Jean Dupont", ""));
    }
}

