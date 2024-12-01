package mma.admin.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomIdTest {
    RoomId roomId;

    @BeforeEach
    void setUp() {
       roomId=new RoomId("LC1");
    }

    //Vérifie l'affichage d'un roomId
    @Test
    void should_return_string_format_when_display_room_id() {
        System.out.println(roomId.toString());
        assertEquals(roomId.toString(),"LC1");

    }

    //Vérifie les appels au getters
    @Test
    void should_return_value_of_getters_when_there_are_calling(){
        assertEquals(roomId.id(),"LC1");
    }

}
