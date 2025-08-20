import mma.admin.domains.*;
import mma.admin.domains.interfaces.AvailabilitySearchStrategy;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.domains.interfaces.repository.UsersRepository;
import mma.admin.domains.interfaces.service.*;
import mma.admin.infrastructures.ICSCalendarsExporter;
import mma.admin.infrastructures.ICSCalendarsImporter;
import mma.admin.infrastructures.SimpleAvailabilitySearch;
import mma.admin.infrastructures.dao.UserDAOImpl;
import mma.admin.infrastructures.repository.DBCalendarsRepository;
import mma.admin.infrastructures.repository.DBRoomsRepository;
import mma.admin.infrastructures.repository.DBUsersRepository;
import mma.admin.infrastructures.service.DBReservationService;
import mma.admin.infrastructures.service.DBRoomService;
import mma.admin.infrastructures.service.DBUserService;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalendarsExporterTest {

//    @TempDir
//    Path tmpDir;
//
//    // Utilitaire pour récupérer le chemin final utilisé dans la classe (il ajoute "\\reservation.ics")
//    private Path expectedIcsPath(Path baseDir) {
//        // La classe concatène "\\reservation.ics" (style Windows)
//        // Sur POSIX, "\\" est un caractère valide du nom de fichier → on respecte le comportement exact.
//        return baseDir.resolve("reservation.ics".replace("/", "\\"));
//    }

    DataBaseInitializer db;
    UsersRepository usersRepository;
    CalendarsRepository calRep;
    CalendarsExporter calendarsExp;
    CalendarsImporter calendarsImp;
    String path="src/test/resources";

    @BeforeEach
    void setUp() throws SQLException {

        db = new DataBaseInitializer();
        db.clearTable();
        db.createTable();

        db.addUserExample();
        db.addRoomExample();
        db.addServiceExample();

        UserService DBUserService = new DBUserService(db.getSQLDataStorage());

        RoomService DBRoomService = new DBRoomService(db.getSQLDataStorage());
        ReservationService reservationsService = new DBReservationService(db.getSQLDataStorage());

        UsersRepository userRep = new DBUsersRepository(DBUserService);
        RoomsRepository roomsRep = new DBRoomsRepository(DBRoomService);


        Map<RoomId, Agenda> calendars=new LinkedHashMap<>();

        for (Room r : roomsRep.getRooms()) {
            calendars.put(r.roomId(),new Agenda(r));
        }
        AvailabilitySearchStrategy availabilitySearchStrategy = new SimpleAvailabilitySearch();
        calRep = new DBCalendarsRepository(reservationsService, calendars,availabilitySearchStrategy);

        calRep.addAllReservation(db.getReservationsExample());

        calendarsExp=new ICSCalendarsExporter(userRep);
        calendarsImp=new ICSCalendarsImporter(calRep);

    }

    @Test
    void test_import_export() throws IOException, ParseException, SQLException {

        calendarsExp.registerInCalendar(path,db.getReservationsExample());
        db.clearTableReservation();
        calendarsImp.readReservations(path);

        assertEquals(calRep.getNbrReservations(), 3);
    }

    @Test
    void testEnsureFileExists_parentNull() throws IOException, ParseException {
        Path tmpFile = Paths.get(path+"/reservation.ics");
        Files.delete(tmpFile);
        calendarsExp.registerInCalendar(path,db.getReservationsExample());
         // couvre branch parent == null
        assertTrue(Files.exists(tmpFile));
    }

    @Test
    void testRegisterInCalendar_catchIOException() throws Exception {
        UsersRepository repo = mock(UsersRepository.class);
        ICSCalendarsExporter exp = new ICSCalendarsExporter(repo);

        // Utiliser un répertoire interdit → provoque IOException
        String forbiddenPath = "/root"; // sous Linux, pas accessible
        assertThrows(NoSuchFileException.class,
                () -> exp.registerInCalendar(forbiddenPath, List.of()));
    }

    @Test
    void testRegisterInCalendar_emptyReservations() throws Exception {

        calendarsExp.registerInCalendar(path, List.of());
        Path tmpFile = Paths.get(path+"/reservation.ics");
        assertTrue(Files.exists(tmpFile));

        String content = Files.readString(tmpFile);
        assertFalse(content.contains("BEGIN:VEVENT")); // pas d’événement
    }

    @Test
    void testRegisterInCalendar_ioException() {
        UsersRepository repo = mock(UsersRepository.class);
        ICSCalendarsExporter exporter = new ICSCalendarsExporter(repo);

        // On pointe vers un répertoire invalide (/root non accessible sur Linux, NUL sur Windows)
        String forbiddenPath = System.getProperty("os.name").toLowerCase().contains("win")
                ? "NUL" : "/root";

        assertThrows(NoSuchFileException.class,
                () -> exporter.registerInCalendar(forbiddenPath, List.of()));
    }

//    @Test
//    void testToEvent_withValidUser() throws Exception {
//        UsersRepository repo = mock(UsersRepository.class);
//        when(repo.getUser("U1")).thenReturn(createUser("U1"));
//
//        ICSCalendarsExporter exporter = new ICSCalendarsExporter(repo);
//
//        Reservation res = createReservation("LB2", "U1");
//
//        // accès via réflexion pour tester la méthode privée
//        var method = ICSCalendarsExporter.class.getDeclaredMethod("toEvent", User.class, Reservation.class);
//        method.setAccessible(true);
//
//        Object event = method.invoke(exporter, createUser("U1"), res);
//        assertNotNull(event);
//        assertTrue(event.toString().contains("Réunion test"));
//    }

    @AfterEach
    void cleanup() throws SQLException {
        //db.clearTable();
        db.connection.close();
    }
}
