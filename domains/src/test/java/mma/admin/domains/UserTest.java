package mma.admin.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user=new User("A123456","Jean Dupont","j.dupont@helmo.be");
    }

    //Vérifie l'affichage d'un user
    @Test
    void should_return_string_format_when_display_user(){
        assertEquals(user.toString(),"Jean Dupont (A123456, j.dupont@helmo.be)");
    }

    //Vérifie les appels au getters
    @Test
    void should_return_value_of_getters_when_there_are_calling(){
        assertEquals(user.getFullName(),"Jean Dupont");
        assertEquals(user.getEmail(),"j.dupont@helmo.be");
        assertEquals(user.getMatricule(),"A123456");
    }

}
