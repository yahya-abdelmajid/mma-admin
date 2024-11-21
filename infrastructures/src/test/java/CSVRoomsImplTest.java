import mma.admin.domains.Room;
import mma.admin.infrastructures.csv.CsvRoomsReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVRoomsImplTest {
    private Set<Room> rooms;
    private CsvRoomsReader csvRooms;

    @BeforeEach
    public void setUp() {
        // Création d'un mock
        //csvRooms = Mockito.mock(CsvRoomsImpl.class);
        //csvRooms.setCsvFile("src/test/resources/rooms.csv");
        Path path= Paths.get("../infrastructures/src/test/resources/rooms.csv");

        csvRooms=new CsvRoomsReader("../infrastructures/src/test/resources/rooms.csv");

        csvRooms.readAllRooms();
        rooms=csvRooms.getRooms();
        System.out.println(rooms.toString());
    }
        @Test
        void Room_is_exist() {

            assertEquals(true, csvRooms.exists("LC1"));
            assertEquals(4,csvRooms.getRooms().size());


        }





}
