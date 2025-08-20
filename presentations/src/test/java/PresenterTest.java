import mma.admin.domains.*;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.domains.interfaces.repository.UsersRepository;
import mma.admin.domains.interfaces.service.CalendarsExporter;
import mma.admin.domains.interfaces.service.CalendarsImporter;
import mma.admin.presentations.IView;
import mma.admin.presentations.Presenter;
import mma.admin.presentations.ReservationT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PresenterTest {

    private IView view;
    private CalendarsRepository calendarsRep;
    private UsersRepository usersRep;
    private RoomsRepository roomsRep;
    private CalendarsExporter calendarsExporter;
    private CalendarsImporter calendarsImporter;
    private Presenter presenter;

    @BeforeEach
    void givenMocks_whenSetUp_thenPresenterIsWired() {
        // Arrange
        view = mock(IView.class);
        calendarsRep = mock(CalendarsRepository.class);
        usersRep = mock(UsersRepository.class);
        roomsRep = mock(RoomsRepository.class);
        calendarsExporter = mock(CalendarsExporter.class);
        calendarsImporter = mock(CalendarsImporter.class);

        // Act
        presenter = new Presenter(view, calendarsRep, usersRep, roomsRep, calendarsExporter, calendarsImporter);

        // Assert
        verify(view).setPresenter(presenter);
    }

    // ---------- addReservation ----------

    @Test
    void givenValidReservationDto_whenAddReservation_thenAddsReservationAndPrestations() throws Exception {
        // Arrange
        LocalDateTime start = LocalDate.now().plusDays(1).atTime(9, 0);
        LocalDateTime end   = LocalDate.now().plusDays(1).atTime(10, 0);

        ReservationT dto = mock(ReservationT.class);
        when(dto.roomId()).thenReturn("LC1");
        when(dto.matricule()).thenReturn("U1");
        when(dto.start()).thenReturn(start);
        when(dto.end()).thenReturn(end);
        when(dto.description()).thenReturn("Réunion");
        when(dto.expected()).thenReturn(4);
        when(dto.services()).thenReturn("Cafe;Projecteur");

        when(calendarsRep.getNbrReservations()).thenReturn(0);
        when(usersRep.exists("U1")).thenReturn(true);
        when(roomsRep.capacityRoomIsNotExceeded("LC1", 4)).thenReturn(true);
        when(calendarsRep.exists(any(Reservation.class))).thenReturn(false);
        when(roomsRep.exists("LC1")).thenReturn(true);

        Prestation p1 = new Prestation(1,"Cafe");
        Prestation p2 = new Prestation(2,"Projecteur");
        when(calendarsRep.getPrestations("Cafe;Projecteur")).thenReturn((List<Prestation>) Set.of(p1, p2));

        // Act
        presenter.addReservation(dto);

        // Assert
        ArgumentCaptor<Reservation> cap = ArgumentCaptor.forClass(Reservation.class);
        verify(calendarsRep).addReservation(cap.capture());
        Reservation saved = cap.getValue();

        assertEquals("LC1", saved.getRoomId().toString());
        assertEquals("U1", saved.getPersonId());
        assertEquals("Réunion", saved.getDescription());
        assertEquals(4, saved.getExpectedPerson());
        String prest = saved.prestationsToString();
        assertTrue(prest.contains("Cafe"));
        assertTrue(prest.contains("Projecteur"));

        verify(view, never()).displayMessage("Matricule inconnu");
        verify(view, never()).displayMessage("Capacité insuffisante");
    }

    @Test
    void givenEmptyServices_whenAddReservation_thenSkipsPrestationsAndAddsReservation() throws Exception {
        // Arrange
        LocalDateTime start = LocalDate.now().plusDays(1).atTime(8, 30);
        LocalDateTime end   = LocalDate.now().plusDays(1).atTime(9, 0);

        ReservationT dto = mock(ReservationT.class);
        when(dto.roomId()).thenReturn("LC1");
        when(dto.matricule()).thenReturn("U1");
        when(dto.start()).thenReturn(start);
        when(dto.end()).thenReturn(end);
        when(dto.description()).thenReturn("Daily");
        when(dto.expected()).thenReturn(2);
        when(dto.services()).thenReturn("");

        when(calendarsRep.getNbrReservations()).thenReturn(5);
        when(usersRep.exists("U1")).thenReturn(true);
        when(roomsRep.capacityRoomIsNotExceeded("LC1", 2)).thenReturn(true);
        when(calendarsRep.exists(any())).thenReturn(false);
        when(roomsRep.exists("LC1")).thenReturn(true);

        // Act
        presenter.addReservation(dto);

        // Assert
        verify(calendarsRep).addReservation(any(Reservation.class));
        verify(calendarsRep, never()).getPrestations(anyString());
    }

    @Test
    void givenUnknownUser_whenAddReservation_thenShowsMessageAndDoesNotAdd() throws Exception {
        // Arrange
        ReservationT dto = mock(ReservationT.class);
        when(dto.roomId()).thenReturn("LC1");
        when(dto.matricule()).thenReturn("X999");
        when(dto.start()).thenReturn(LocalDate.now().plusDays(1).atTime(9, 0));
        when(dto.end()).thenReturn(LocalDate.now().plusDays(1).atTime(10, 0));
        when(dto.description()).thenReturn("Réunion");
        when(dto.expected()).thenReturn(3);
        when(dto.services()).thenReturn("");

        when(calendarsRep.getNbrReservations()).thenReturn(0);
        when(usersRep.exists("X999")).thenReturn(false);

        // Act
        presenter.addReservation(dto);

        // Assert
        verify(view).displayMessage("Matricule inconnu");
        verify(calendarsRep, never()).addReservation(any());
    }

    @Test
    void givenCapacityExceeded_whenAddReservation_thenShowsMessageAndDoesNotAdd() throws Exception {
        // Arrange
        ReservationT dto = mock(ReservationT.class);
        when(dto.roomId()).thenReturn("LC1");
        when(dto.matricule()).thenReturn("U1");
        when(dto.start()).thenReturn(LocalDate.now().plusDays(1).atTime(9, 0));
        when(dto.end()).thenReturn(LocalDate.now().plusDays(1).atTime(10, 0));
        when(dto.description()).thenReturn("Réunion");
        when(dto.expected()).thenReturn(50);
        when(dto.services()).thenReturn("");

        when(calendarsRep.getNbrReservations()).thenReturn(0);
        when(usersRep.exists("U1")).thenReturn(true);
        when(roomsRep.capacityRoomIsNotExceeded("LC1", 50)).thenReturn(false);

        // Act
        presenter.addReservation(dto);

        // Assert
        verify(view).displayMessage("Capacité insuffisante");
        verify(calendarsRep, never()).addReservation(any());
    }

    @Test
    void givenExistingReservationConflict_whenAddReservation_thenShowsMessageAndDoesNotAdd() throws Exception {
        // Arrange
        ReservationT dto = mock(ReservationT.class);
        when(dto.roomId()).thenReturn("LC1");
        when(dto.matricule()).thenReturn("U1");
        when(dto.start()).thenReturn(LocalDate.now().plusDays(1).atTime(9, 0));
        when(dto.end()).thenReturn(LocalDate.now().plusDays(1).atTime(10, 0));
        when(dto.description()).thenReturn("Réunion");
        when(dto.expected()).thenReturn(3);
        when(dto.services()).thenReturn("");

        when(calendarsRep.getNbrReservations()).thenReturn(0);
        when(usersRep.exists("U1")).thenReturn(true);
        when(roomsRep.capacityRoomIsNotExceeded("LC1", 3)).thenReturn(true);
        when(calendarsRep.exists(any())).thenReturn(true);

        // Act
        presenter.addReservation(dto);

        // Assert
        verify(view).displayMessage("Créneau de 09:00 déjà occupé");
        verify(calendarsRep, never()).addReservation(any());
    }

    @Test
    void givenNonPositiveExpected_whenAddReservation_thenCatchesDomainExceptionAndDisplaysMessage() throws Exception {
        // Arrange
        ReservationT dto = mock(ReservationT.class);
        when(dto.roomId()).thenReturn("LC1");
        when(dto.matricule()).thenReturn("U1");
        when(dto.start()).thenReturn(LocalDate.now().plusDays(1).atTime(9, 0));
        when(dto.end()).thenReturn(LocalDate.now().plusDays(1).atTime(10, 0));
        when(dto.description()).thenReturn("Réunion");
        when(dto.expected()).thenReturn(0); // -> NoPositiveCapacityException dans Reservation
        when(dto.services()).thenReturn("");

        when(calendarsRep.getNbrReservations()).thenReturn(0);

        // Act
        presenter.addReservation(dto);

        // Assert
        verify(view).displayMessage("La capacité doit être strictement positif.");
        verify(calendarsRep, never()).addReservation(any());
    }

    // ---------- displayHeader ----------

    @Test
    void givenFormatter_whenDisplayHeader_thenHeaderLineIsPrinted() {
        // Arrange
        var fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        // Act
        presenter.displayHeader(fmt);

        // Assert
        ArgumentCaptor<String> cap = ArgumentCaptor.forClass(String.class);
        verify(view).displayMessage(cap.capture());
        String header = cap.getValue();
        assertTrue(header.startsWith(String.format("%-10s", "Salle")));
        assertTrue(header.contains("08:00"));
        assertTrue(header.contains("16:30"));
    }

    @Test
    void givenUnknownRoom_whenDisplayReservationIfExist_thenShowsRoomNotFoundMessage() {
        // Arrange
        when(roomsRep.exists("LCX")).thenReturn(false);

        // Act
        presenter.displayReservationIfExist("LCX", LocalDateTime.of(2030, 1, 1, 9, 0));

        // Assert
        verify(view).displayMessage("La salle n'existe pas");
    }

    @Test
    void givenOutOfWorkingHours_whenDisplayReservationIfExist_thenShowsOutOfHoursMessage() {
        // Arrange
        when(roomsRep.exists("LC1")).thenReturn(true);

        // Act
        presenter.displayReservationIfExist("LC1", LocalDateTime.of(2030, 1, 1, 7, 30));

        // Assert
        verify(view).displayMessage("Le créneau horaire est en dehors des heures d'ouverture!");
    }

    @Test
    void givenNoReservationFound_whenDisplayReservationIfExist_thenShowsNoMatch() {
        // Arrange
        when(roomsRep.exists("LC1")).thenReturn(true);
        when(calendarsRep.getReservation(eq("LC1"), any(LocalDateTime.class))).thenReturn(null);

        // Act
        presenter.displayReservationIfExist("LC1", LocalDateTime.of(2030, 1, 1, 9, 0));

        // Assert
        verify(view).displayMessage("Aucune correspondance trouve");
    }

    @Test
    void givenReservationFound_whenDisplayReservationIfExist_thenDisplaysComposedMessage() {
        // Arrange
        when(roomsRep.exists("LC1")).thenReturn(true);

        Reservation res = new Reservation(
                1, "LC1", "U1",
                new TimeSlot(LocalDate.of(2030, 1, 1), LocalTime.of(9, 0), LocalTime.of(9, 30)),
                "Réu", 2
        );
        when(calendarsRep.getReservation(eq("LC1"), any(LocalDateTime.class))).thenReturn(res);

        Room room = new Room(new RoomId("LC1"), "Salle A", 10);
        when(roomsRep.getRoom(new RoomId("LC1"))).thenReturn(room);

        User user = new User("U1", "Jean Dupont", "jd@mail.com");
        when(usersRep.getUser("U1")).thenReturn(user);

        // Act
        presenter.displayReservationIfExist("LC1", LocalDateTime.of(2030, 1, 1, 9, 0));

        // Assert
        verify(view).displayMessage(room + res.toString() + user + res.prestationsToString());
    }

    // ---------- displayRoomRequest ----------

    @Test
    void givenAvailabilities_whenDisplayRoomRequest_thenShowsHeaderAndLimitedRows() {
        // Arrange
        Availability a1 = mock(Availability.class);
        Availability a2 = mock(Availability.class);
        Availability a3 = mock(Availability.class);

        TimeSlot ts1 = new TimeSlot(LocalDate.of(2030, 1, 1), LocalTime.of(10, 0), LocalTime.of(11, 0));
        TimeSlot ts2 = new TimeSlot(LocalDate.of(2030, 1, 2), LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot ts3 = new TimeSlot(LocalDate.of(2030, 1, 3), LocalTime.of(8, 30), LocalTime.of(9, 30));

        when(a1.getRoom()).thenReturn(new RoomId("LC1"));
        when(a1.getTimeSlot()).thenReturn(ts1);
        when(a2.getRoom()).thenReturn(new RoomId("LC2"));
        when(a2.getTimeSlot()).thenReturn(ts2);
        when(a3.getRoom()).thenReturn(new RoomId("LC3"));
        when(a3.getTimeSlot()).thenReturn(ts3);

        when(calendarsRep.getAvailabilities(any(TimeSlot.class), any(LocalDate.class), any(Duration.class), anyInt()))
                .thenReturn(List.of(a1, a2, a3));

        // Act
        presenter.displayRoomRequest(LocalDate.of(2030, 1, 1), 5, Duration.ofHours(1), 2);

        // Assert
        verify(view).displayMessage(String.format("%-10s%-15s%-20s%-18s", "Salle","Date","Heure de debut","Heure de fin"));
        verify(view, atLeast(2)).displayMessage(argThat(s -> s.startsWith("LC")));
    }

    // ---------- exportReservationRoom ----------

    @Test
    void givenInvalidRoom_whenExportReservationRoom_thenShowsMessageAndDoesNotExport() throws Exception {
        // Arrange
        when(roomsRep.exists("LCX")).thenReturn(false);

        // Act
        presenter.exportReservationRoom("LCX", LocalDate.of(2030,1,1), LocalDate.of(2030,1,2), "/tmp");

        // Assert
        verify(view).displayMessage("Salle non trouvée");
        verifyNoInteractions(calendarsExporter);
    }

    @Test
    void givenInvalidPeriod_whenExportReservationRoom_thenShowsMessageAndDoesNotExport() throws Exception {
        // Arrange
        when(roomsRep.exists("LC1")).thenReturn(true);

        // Act
        presenter.exportReservationRoom("LC1", LocalDate.of(2030,1,2), LocalDate.of(2030,1,1), "/tmp");

        // Assert
        verify(view).displayMessage("Période invalide");
        verifyNoInteractions(calendarsExporter);
    }

    @Test
    void givenNonExistingPath_whenExportReservationRoom_thenShowsMessageAndDoesNotExport() throws Exception {
        // Arrange
        when(roomsRep.exists("LC1")).thenReturn(true);

        // Act
        presenter.exportReservationRoom("LC1", LocalDate.of(2030,1,1), LocalDate.of(2030,1,2), "/path/does/not/exist");

        // Assert
        verify(view).displayMessage("Chemin vers le fichier incorrect");
        verifyNoInteractions(calendarsExporter);
    }

    @Test
    void givenAllChecksOk_whenExportReservationRoom_thenCallsExporter(@TempDir Path dir) throws Exception {
        // Arrange
        when(roomsRep.exists("LC1")).thenReturn(true);
        Path existing = dir; // exists

        Agenda agenda = mock(Agenda.class);
        List<Reservation> res = List.of(
                new Reservation(1, "LC1", "U1",
                        new TimeSlot(LocalDate.of(2030,1,1), LocalTime.of(9,0), LocalTime.of(10,0)),
                        "Réu", 2)
        );
        when(calendarsRep.getAgenda(new RoomId("LC1"))).thenReturn(agenda);
        when(agenda.getReservations(LocalDate.of(2030,1,1), LocalDate.of(2030,1,2))).thenReturn(res);

        // Act
        presenter.exportReservationRoom("LC1", LocalDate.of(2030,1,1), LocalDate.of(2030,1,2), existing.toString());

        // Assert
        verify(calendarsExporter).registerInCalendar(existing.toString(), res);
    }

    // ---------- importReservations ----------

    @Test
    void givenConflictingReservations_whenImportReservations_thenShowsMessageAndStops() throws Exception {
        // Arrange
        List<Reservation> imported = List.of(
                new Reservation(1, "LC1", "U1",
                        new TimeSlot(LocalDate.now().plusDays(1), LocalTime.of(9,0), LocalTime.of(10,0)),
                        "Réu", 2)
        );
        when(calendarsImporter.readReservations("abs/path")).thenReturn(imported);
        when(roomsRep.allRoomsExist(imported)).thenReturn(true);
        when(calendarsRep.allExists(imported)).thenReturn(true);

        // Act
        presenter.importReservations("abs/path");

        // Assert
        verify(view).displayMessage("Une ou plusieurs occupations entrent en conflit avec des occupations déjà enregistrées");
        verify(calendarsRep, never()).addAllReservation(anyList());
    }

    @Test
    void givenValidImportedReservations_whenImportReservations_thenBulkAddToRepository() throws Exception {
        // Arrange
        List<Reservation> imported = List.of(
                new Reservation(1, "LC1", "U1",
                        new TimeSlot(LocalDate.now().plusDays(1), LocalTime.of(9,0), LocalTime.of(10,0)),
                        "Réu", 2)
        );
        when(calendarsImporter.readReservations("abs/path")).thenReturn(imported);
        when(roomsRep.allRoomsExist(imported)).thenReturn(true);
        when(calendarsRep.allExists(imported)).thenReturn(false);

        // Act
        presenter.importReservations("abs/path");

        // Assert
        verify(calendarsRep).addAllReservation(imported);
    }
}
