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

class CalendarsImporterTest {

    DataBaseInitializer db;
    UsersRepository usersRepository;
    CalendarsRepository calRep;
    CalendarsExporter calendarsExp;
    CalendarsImporter calendarsImp;
    String path="src/test/resources";

    @TempDir
    Path tmpDir;

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
    void test_import_same_reservation() throws IOException, ParseException, SQLException {

        calendarsExp.registerInCalendar(path,db.getReservationsExample());
        List<Reservation> reservations=calendarsImp.readReservations(path);
        calRep.addAllReservation(reservations);

        assertEquals(calRep.getNbrReservations(), 3);
    }

    @Test
    void test_import_missingFile_returnsEmptyList() {
        Path missingDir = Paths.get("subdir");
        // Aucun .ics n’existe
        List<Reservation> imported = calendarsImp.readReservations(missingDir.toString());
        assertTrue(imported.isEmpty());
    }

    @Test
    void test_import_invalidFile_returnsEmptyList() throws IOException {
        Path invalidFile = tmpDir.resolve("reservation.ics");
        Files.writeString(invalidFile, "NOT A VALID ICS");

        List<Reservation> imported = calendarsImp.readReservations(invalidFile.toString());
        assertTrue(imported.isEmpty());
    }

    @AfterEach
    void cleanup() throws SQLException {
        //db.clearTable();
        db.connection.close();
    }
}
