package mma.admin.infrastructures.repository;

import mma.admin.domains.*;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.infrastructures.ReservationDTO;
import mma.admin.infrastructures.dto.ReservationMapper;
import mma.admin.infrastructures.service.PrestationService;
import mma.admin.infrastructures.service.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DBCalendarsRepository implements CalendarsRepository {
    private static final Logger LOGGER = LogManager.getLogger();
    private Map<RoomId, Agenda> calendars;
    private ReservationService reservationsService;
    private PrestationService prestationsService;
    private List<Prestation> prestations;
    private List<ReservationPrestation> reservationPrestations;

    public DBCalendarsRepository(ReservationService reservationsService, PrestationService prestationService, List<Room> rooms) throws SQLException {
        this.calendars = new LinkedHashMap<>();
        this.reservationsService = reservationsService;
        this.prestationsService=prestationService;
        initializeDictionary(rooms);
        this.prestations = prestationService.getAllPrestations();
        readAllReservations();
        this.reservationPrestations=prestationService.getAllReservationService();
    }

    public Map<RoomId, Agenda> getCalendars() {
        return calendars;
    }

    public List<Prestation> getPrestations() {
        return prestations;
    }

    @Override
    public List<Prestation> getPrestations(String services) {
            String[] service = services.split(",");
            List<Integer> servicesInt=Arrays.stream(service)
                    .map(Integer::parseInt)
                    .toList();
            return prestations.stream()
                    .filter(prestation -> servicesInt.contains(prestation.getId()))
                    .collect(Collectors.toList());
    }

    @Override
    public int getNbrReservations() {
        return calendars.values().stream()
                .mapToInt(agenda -> agenda.getReservations().size())
                .sum();
    }

    @Override
    public boolean exists(Reservation reservation) {
        for(Agenda agenda : calendars.values()) {
            if(agenda.getReservations().contains(reservation)) return true;
        }
        return false;
    }

    @Override
    public Collection<Agenda> getAgendas(){
        return calendars.values();
    }


    public Reservation getReservation(String roomId,TimeSlot timeSlot) {
        ReservationDTO rDTO = new ReservationDTO(roomId,timeSlot);
        for(Agenda agenda : calendars.values()) {
            for(Reservation r : agenda.getReservations()) {
                if(r.getRoomId().equals(rDTO.getRoomId())&&
                r.getTimeSlot().equals(rDTO.getTimeSlot())) {return r;}
            }
        }
        return null;
    }

    @Override
    public void addReservation(Reservation reservation) throws SQLException {
        reservationsService.addReservation(reservation);
        prestationsService.addReservationAndService(reservation);
    }

    // Méthode pour initialiser le dictionnaire
    private void initializeDictionary(Collection<Room> room) {
        for (Room r : room) {
            calendars.put(r.roomId(), new Agenda(r));
        }
    }

    public void readAllReservations() throws SQLException {
        List<Reservation> reservations = reservationsService.getAllReservations();
        List<ReservationPrestation> reservationPrestations=prestationsService.getAllReservationService();
        Prestation p=new Prestation();

        for(ReservationPrestation rp:reservationPrestations){
            p=prestations.get(rp.serviceId()-1);
            if(p!=null){
                reservations.get(rp.reservationId()-1).addPrestation(p);
            }
        }

        for (Reservation r : reservations) {
            calendars.get(r.getRoomId()).add(r);
        }
    }

    @Override
    public List<Availability> getAvailabilities(TimeSlot workingHours ,LocalDate date, Duration duration, int roomCapacity) {
        List<Availability> availabilities = new ArrayList<>();
        LocalDate end = date.plusDays(2);
        LocalDate initial = date;

        for (Agenda agenda : calendars.values()) {
            if (agenda.getRoom().capacity() >= roomCapacity) {
                while (date.isBefore(end) || date.isEqual(end)) {
                    //ajoute une liste de disponibilité par date
                    agenda.searchAvailabilities(workingHours,date, duration, roomCapacity);
                    date = date.plusDays(1);
                }
                availabilities.addAll(agenda.getAvailabilities());
                date = initial;
            }
        }

        return availabilities;
    }
}
