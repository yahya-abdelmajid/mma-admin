package mma.admin.infrastructures.repository;

import mma.admin.domains.*;
import mma.admin.domains.interfaces.AvailabilitySearchStrategy;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.service.ReservationService;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.*;

public class DBCalendarsRepository implements CalendarsRepository {
    //private static final Logger LOGGER = LogManager.getLogger();

    private final ReservationService reservationsService;
    private final Map<RoomId, Agenda> calendars;
    private final List<Prestation> prestations;
    private final AvailabilitySearchStrategy availabilitySearchStrategy;

    public DBCalendarsRepository(ReservationService reservationsService, Map<RoomId, Agenda> calendars,AvailabilitySearchStrategy availabilitySearchStrategy) throws SQLException {
        this.calendars = calendars;
        this.reservationsService = reservationsService;
        this.prestations = reservationsService.getAllPrestations();
        this.availabilitySearchStrategy = availabilitySearchStrategy;
        readAllReservations();
    }

    @Override
    public Map<RoomId, Agenda> getCalendars() {
        return calendars;
    }

    @Override
    public Agenda getAgenda(RoomId roomId) {
        return calendars.get(roomId);
    }

    @Override
    public List<Prestation> getPrestations(String services) {
        if (!services.matches("^[1-4](,[1-4])*$")) {
            return List.of();
        }

        final String hay = "," + services + ",";
        return prestations.stream()
                .filter(p -> hay.contains("," + p.id() + ","))
                .toList(); // immuable, OK si on ne tries/modifies pas ensuite
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
            if(agenda.getReservations().contains(reservation)) {return true;}
        }
        return false;
    }

    @Override
    public boolean allExists(List<Reservation> reservations) {
        for(Reservation reservation : reservations){
            if(!exists(reservation)){return false;}
        }
        return true;
    }

    @Override
    public Reservation getReservation(String roomId, LocalDateTime dateTime) {

        for(Agenda agenda : calendars.values()) {
            for(Reservation r : agenda.getReservations()) {

                TimeSlot ts = r.getTimeSlot();
               // boolean isAfter=(ts.getStart().isAfter(start) || start.equals(lt))&&lt.isBefore(end);
                if(r.getRoomId().id().equals(roomId)&&
                ts.isAfter(dateTime.toLocalTime())&&
                r.isDate(dateTime.toLocalDate())) {
                    return r;}
            }
        }
        return null;
    }

    @Override
    public void addReservation(Reservation reservation) throws SQLException {
        reservationsService.addReservationWithServices(reservation);
        calendars.get(reservation.getRoomId()).add(reservation);
    }

    @Override
    public void addAllReservation(List<Reservation> reservations) throws SQLException {
        for(Reservation reservation : reservations){
            reservationsService.addReservationWithServices(reservation);
            calendars.get(reservation.getRoomId()).add(reservation);
        }
    }

    @Override
    public final void readAllReservations(){

        List<Reservation> reservations = reservationsService.getAllReservations();
        List<ReservationPrestation> reservationPrestations=reservationsService.getAllReservationService();
        Prestation p;
        List<Prestation> prestations=reservationsService.getAllPrestations();

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

    // utilisation
    @Override public List<Availability> getAvailabilities(TimeSlot workingHours, LocalDate date, TemporalAmount duration, int roomCapacity) {
        return calendars.values().stream()
                .filter(agenda -> agenda.getRoom().capacity() >= roomCapacity)
                .flatMap(agenda -> availabilitySearchStrategy
                        .searchAvailabilities(agenda, workingHours, date, duration, roomCapacity)
                        .stream())
                .toList();
    }

}
