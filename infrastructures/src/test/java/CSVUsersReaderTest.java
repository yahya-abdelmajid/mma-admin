import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.UsersService;
import mma.admin.infrastructures.CsvRoomsReader;
import mma.admin.infrastructures.CsvUsersReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVUsersReaderTest {

    private Set<User> users;
    private UsersService csvUsers;

    @BeforeEach
    public void setUp() {
        // Création d'un mock
        //csvRooms = Mockito.mock(CsvRoomsImpl.class);
        //csvRooms.setCsvFile("src/test/resources/rooms.csv");
        //Path path= Paths.get("../infrastructures/src/test/resources/rooms.csv");

        csvUsers=new CsvUsersReader("../infrastructures/src/test/resources/users.csv");
        csvUsers.readAllUsers();
        users =csvUsers.getUsers();
        System.out.println(users.size());

        System.out.println(users.toString());
    }
    @Test
    void Users_is_exist() {
        System.out.println(users.toString());
        assertEquals(true, csvUsers.exists("A123456"));
        assertEquals(10,csvUsers.getUsers().size());
    }
}
