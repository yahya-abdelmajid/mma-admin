import mma.admin.domains.*;
import mma.admin.domains.interfaces.AvailabilitySearchStrategy;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.service.ReservationService;
import mma.admin.infrastructures.SimpleAvailabilitySearch;
import mma.admin.infrastructures.repository.DBCalendarsRepository;
import mma.admin.infrastructures.service.DBReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarsRepositoryTest {


    DataBaseInitializer db;
    CalendarsRepository calendarsRepository;
    Map<RoomId, Agenda> calendars;

    @Mock
    private ReservationService mockReservationService;

    @Mock
    private Map<RoomId, Agenda> mockCalendars;

    private List<Prestation> prestations;
    @BeforeEach
    void setUp() throws SQLException {
       db = new DataBaseInitializer();
       db.clearTable();
       db.createTable();

       //ajout Room
        List<Room> rooms = List.of(
                new Room(new RoomId("LC1"), "Learning Center 1", 20),
                new Room(new RoomId("LC2"), "Learning Center 2", 20),
                new Room(new RoomId("LB1"), "Learning Box 1", 5),
                new Room(new RoomId("LB2"), "Learning Box 2", 5)
        );

        db.addRoom(rooms);

        //ajout Prestation
        prestations = List.of(
                new Prestation(1, "Café et boissons"),
                new Prestation(2, "Nourriture"),
                new Prestation(3, "Projecteur"),
                new Prestation(4, "Matériel sonore")
        );
        db.addPrestation(prestations);
        // Ajout des utilisateurs

        List<User> users = new ArrayList<>();

        // Ajout des utilisateurs
        users.add(new User("A123456", "Jean Dupont", "j.dupont@helmo.be"));
        users.add(new User("B234567", "Marie Dubois", "m.dubois@helmo.be"));
        users.add(new User("C345678", "Pierre Martin", "p.martin@helmo.be"));
        users.add(new User("D456789", "Sophie Lambert", "s.lambert@helmo.be"));
        users.add(new User("E567890", "Luc Lefevre", "l.lefevre@helmo.be"));
        users.add(new User("F678901", "Claire Simon", "c.simon@helmo.be"));
        users.add(new User("G789012", "Marc Durand", "m.durand@helmo.be"));
        users.add(new User("H890123", "Julie Bernard", "j.bernard@helmo.be"));
        users.add(new User("I901234", "Paul Richard", "p.richard@helmo.be"));
        users.add(new User("J012345", "Emma Petit", "e.petit@helmo.be"));

        db.addUser(users);

        calendars=new LinkedHashMap<>();

        for (Room r : rooms) {
            calendars.put(r.roomId(),new Agenda(r));
        }

        ReservationService reservationService= new DBReservationService(db.getSQLDataStorage());
        AvailabilitySearchStrategy availabilitySearchStrategy = new SimpleAvailabilitySearch();
        calendarsRepository=new DBCalendarsRepository(reservationService,calendars,availabilitySearchStrategy);

        TimeSlot timeSlot1 = new TimeSlot(LocalDate.of(2024,12,30) ,LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reservation reservation1 = new Reservation(1, "LC1", "C345678", timeSlot1, "Nickoff",10);

        TimeSlot timeSlot2 = new TimeSlot(LocalDate.of(2024,12,30) ,LocalTime.of(10, 0), LocalTime.of(11, 0));
        Reservation reservation2 = new Reservation(2, "LC1", "A123456", timeSlot2, "Nickoff",10);

        TimeSlot timeSlot3 = new TimeSlot(LocalDate.of(2024,12,30) ,LocalTime.of(10, 0), LocalTime.of(12, 0));
        Reservation reservation3 = new Reservation(3, "LB2", "C345678", timeSlot3, "Nickoff",10);

        calendarsRepository.addReservation(reservation1);
        calendarsRepository.addReservation(reservation2);
        calendarsRepository.addReservation(reservation3);

        calendarsRepository.readAllReservations();
    }

    @Test
    void should_return_correct_prestations_given_valid_service_ids_when_get_prestations_then_return_filtered_prestations() {
        String services = "1,2";
        List<Prestation> result = calendarsRepository.getPrestations(services);

        assertEquals(2, result.size());
        assertTrue(result.contains(prestations.get(0)));
        assertTrue(result.contains(prestations.get(1)));
    }

    @Test
    void should_return_empty_list_given_nonexistent_service_ids_when_get_prestations_then_return_empty_list() {

        String services = "5,6";
        String services2 = "1,2_3,4";

        List<Prestation> result = calendarsRepository.getPrestations(services);
        List<Prestation> result2 = calendarsRepository.getPrestations(services2);

        assertTrue(result.isEmpty());
        assertTrue(result2.isEmpty());

    }

    @Test
    void should_return_correct_reservation_count_given_calendars_initialized_when_get_nbr_reservations_then_return_correct_count() {

        int result = calendarsRepository.getNbrReservations();

        assertEquals(3, result);
    }

    @Test
    void should_return_true_given_reservation_exists_when_exists_then_return_true() {

        TimeSlot timeSlot1 = new TimeSlot(LocalDate.of(2024,12,30) ,LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reservation reservation1 = new Reservation(1, "LC1", "C345678", timeSlot1, "Nickoff",10);

        boolean result = calendarsRepository.exists(reservation1);

        assertTrue(result);
    }

    @Test
    void should_return_false_given_reservation_does_not_exist_when_exists_then_return_false() {

        TimeSlot timeSlot2 = new TimeSlot(LocalDate.of(2024,10,25), LocalTime.of(12, 0), LocalTime.of(15, 0));

        Reservation reservation2 = new Reservation(2, "LC1", "B234567", timeSlot2, "Description",11);

        boolean result2 = calendarsRepository.exists(reservation2);

        assertFalse(result2);
    }

    @Test
    void should_return_reservation_given_matching_room_and_timeslot_when_get_reservation_then_return_reservation() {

        LocalDateTime dateTime=LocalDateTime.of(2024,12,30,9,0,0);
        TimeSlot timeSlot = new TimeSlot(LocalDate.of(2024,12,30), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1, "LC1", "C345678", timeSlot, "Nickoff",10);

        Reservation result = calendarsRepository.getReservation("LC1", dateTime);

        assertTrue(reservation.compareTo(result)==0);
    }

    @Test
    void should_return_null_given_no_matching_room_and_timeslot_when_get_reservation_then_return_null() {
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.of(2024,10,25), LocalTime.of(12, 0), LocalTime.of(15, 0));
        Reservation reservation2 = new Reservation(2, "LC1", "B234567", timeSlot2, "Description",11);

        Reservation result = calendarsRepository.getReservation("LC1",timeSlot2.getStartTime());
        assertNull(result);
    }


    @Test
    void test_insert_select_reservation() {

        Map<RoomId, Agenda> calendars=calendarsRepository.getCalendars();
        List<Reservation> reservations=calendars.get(new RoomId("LC1")).getReservations().stream().toList();

        assertEquals(reservations.get(0).getDescription(), "Nickoff" );
    }

    @Test
    void should_return_correct_availabilities_given_valid_criteria_when_get_availabilities_then_return_availabilities() {
        // Given

        TimeSlot timeSlot1 = new TimeSlot(LocalDate.of(2024,12,30) ,LocalTime.of(11, 0), LocalTime.of(17, 0));
        TimeSlot workingHours=new TimeSlot(LocalTime.of(8, 0), LocalTime.of(17, 0));

        LocalDate date = LocalDate.of(2024,12,30);
        Duration duration = Duration.ofHours(6);
        int roomCapacity = 16;

        List<Availability> availabilities = new ArrayList<>(calendarsRepository.getAvailabilities(workingHours,date,duration,roomCapacity));

        availabilities.sort(Comparator.reverseOrder());

        // Then
        assertEquals(availabilities.get(0).toString(),new Availability(new RoomId("LC1"),timeSlot1).toString());
    }



    @AfterEach
    void cleanup() throws SQLException {
        db.clearTable();
        db.connection.close();
    }
}
