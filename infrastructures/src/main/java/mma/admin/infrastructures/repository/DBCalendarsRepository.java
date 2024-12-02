package mma.admin.infrastructures.repository;

import mma.admin.domains.*;
import mma.admin.domains.interfaces.AvailabilitySearchStrategy;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.service.ReservationService;
import mma.admin.infrastructures.SimpleAvailabilitySearch;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DBCalendarsRepository implements CalendarsRepository {
    //private static final Logger LOGGER = LogManager.getLogger();

    private final ReservationService reservationsService;
    private final Map<RoomId, Agenda> calendars;

    public DBCalendarsRepository(ReservationService reservationsService, Map<RoomId, Agenda> calendars) throws SQLException {
        this.calendars = calendars;
        this.reservationsService = reservationsService;
        //readAllReservations();
    }

    @Override
    public Map<RoomId, Agenda> getCalendars() {
        return calendars;
    }

    @Override
    public List<Prestation> getPrestations(String services) {
        if(!services.matches("^[1-4](,[1-4])*$")){
            return Collections.emptyList();
        }
            String[] service = services.split(",");
            List<Integer> servicesInt=Arrays.stream(service)
                    .map(Integer::parseInt)
                    .toList();

        List<Prestation> prestations=reservationsService.getAllPrestations();
            return prestations.stream()
                    .filter(prestation -> servicesInt.contains(prestation.id()))
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
            if(agenda.getReservations().contains(reservation)) {return true;}
        }
        return false;
    }

    @Override
    public Reservation getReservation(String roomId,TimeSlot timeSlot) {

        for(Agenda agenda : calendars.values()) {
            for(Reservation r : agenda.getReservations()) {
                if(r.getRoomId().id().equals(roomId)&&
                r.getTimeSlot().compareTo(timeSlot)==0) {
                    return r;}
            }
        }
        return null;
    }

    @Override
    public void addReservation(Reservation reservation) throws SQLException {
        reservationsService.addReservation(reservation);
        reservationsService.addReservationAndService(reservation);
        calendars.get(reservation.getRoomId()).add(reservation);
    }

    @Override
    public void readAllReservations(){

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

//    @Override
//    public List<Availability> getAvailabilities(TimeSlot workingHours ,LocalDate date, Duration duration, int roomCapacity) {
//        List<Availability> availabilities = new ArrayList<>();
//        LocalDate end = date.plusDays(2);
//        LocalDate currentDate;
//        for (Agenda agenda : calendars.values()) {
//            if (agenda.getRoom().capacity() >= roomCapacity) {
//                availabilitySearchStrategy.searchAvailabilities(agenda,workingHours,date,duration,roomCapacity);
//                availabilities.addAll(agenda.getAvailabilities());
//            }
//        }
//
//        return availabilities;
//    }

    @Override
    public List<Availability> getAvailabilities(TimeSlot workingHours, LocalDate date, Duration duration, int roomCapacity) {
        AvailabilitySearchStrategy availabilitySearchStrategy= new SimpleAvailabilitySearch();

        return calendars.values().stream()
                .filter(agenda -> agenda.getRoom().capacity() >= roomCapacity)
                .flatMap(agenda -> availabilitySearchStrategy.searchAvailabilities(agenda, workingHours, date, duration, roomCapacity).stream())
                .collect(Collectors.toList());
    }



//    @Override
//    public List<Availability> getAvailabilities(TimeSlot workingHours, LocalDate date, Duration duration, int roomCapacity) {
//        return calendars.values().stream()
//                .filter(agenda -> agenda.getRoom().capacity() >= roomCapacity)
//                .flatMap(agenda -> availabilitySearchStrategy.searchAvailabilities(agenda, workingHours, date, duration, roomCapacity).stream())
//                .collect(Collectors.toList());
//    }
}
