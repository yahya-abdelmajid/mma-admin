package mma.admin.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");
    }

    // ---------- toString / getters ----------
    @Test
    void should_return_string_format_when_display_user() {
        String expected = "Responsable : Jean Dupont (A123456, j.dupont@helmo.be)\n";
        assertEquals(expected, user.toString());
    }

    @Test
    void should_return_value_of_getters_when_there_are_calling() {
        assertEquals("Jean Dupont", user.fullName());
        assertEquals("j.dupont@helmo.be", user.email());
        assertEquals("A123456", user.badge());
    }

    // ---------- equals: contrats & branches ----------
    @Test
    void equals_is_reflexive() {
        assertEquals(user, user); // même instance
    }

    @Test
    void equals_returns_false_when_compared_to_null() {
        assertNotEquals(user, null);
    }

    @Test
    void equals_returns_false_when_compared_to_different_type() {
        assertNotEquals(user, "not a user");
    }

    @Test
    void equals_is_symmetric_and_transitive() {
        User b = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");
        User c = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");

        // symétrie
        assertEquals(user, b);
        assertEquals(b, user);

        // transitivité
        assertEquals(b, c);
        assertEquals(user, c);
    }

    @Test
    void should_return_true_when_users_are_equal() {
        User identicalUser = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");
        assertEquals(user, identicalUser);
    }

    @Test
    void should_return_false_when_users_have_different_badges() {
        User differentBadgeUser = new User("B654321", "Jean Dupont", "j.dupont@helmo.be");
        assertNotEquals(user, differentBadgeUser);
    }

    @Test
    void should_return_false_when_users_have_different_emails() {
        User differentEmailUser = new User("A123456", "Jean Dupont", "jean.dupont@autre.be");
        assertNotEquals(user, differentEmailUser);
    }

    @Test
    void should_return_false_when_users_have_different_names() {
        User differentNameUser = new User("A123456", "John Doe", "j.dupont@helmo.be");
        assertNotEquals(user, differentNameUser);
    }

    // ---------- hashCode: contrats & branches ----------
    @Test
    void hashCode_same_for_equal_users_and_consistent() {
        User identicalUser = new User("A123456", "Jean Dupont", "j.dupont@helmo.be");
        assertEquals(user.hashCode(), identicalUser.hashCode());

        int initial = user.hashCode();
        // cohérence (chaînes immuables, hashCode ne change pas)
        assertEquals(initial, user.hashCode());
        assertEquals(initial, user.hashCode());
    }

    @Test
    void hashCode_different_for_different_users() {
        User differentUser = new User("B654321", "Jean Dupont", "j.dupont@helmo.be");
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }

    // ---------- validations constructeur (null / empty / blank / email invalide) ----------
    @Test
    void should_throw_exception_when_badge_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "Jean Dupont", "j.dupont@helmo.be"));
    }

    @Test
    void should_throw_exception_when_full_name_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", null, "j.dupont@helmo.be"));
    }

    @Test
    void should_throw_exception_when_email_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "Jean Dupont", null));
    }

    @Test
    void should_throw_exception_when_badge_is_empty_or_blank() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "Jean Dupont", "j.dupont@helmo.be"));
        assertThrows(IllegalArgumentException.class, () -> new User("   ", "Jean Dupont", "j.dupont@helmo.be"));
    }

    @Test
    void should_throw_exception_when_full_name_is_empty_or_blank() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "", "j.dupont@helmo.be"));
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "   ", "j.dupont@helmo.be"));
    }

    @Test
    void should_throw_exception_when_email_is_empty_or_blank() {
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "Jean Dupont", ""));
        assertThrows(IllegalArgumentException.class, () -> new User("A123456", "Jean Dupont", "   "));
    }
}
