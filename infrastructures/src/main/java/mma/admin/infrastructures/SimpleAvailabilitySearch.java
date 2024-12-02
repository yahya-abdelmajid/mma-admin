package mma.admin.infrastructures;

import mma.admin.domains.Agenda;
import mma.admin.domains.Availability;
import mma.admin.domains.TimeSlot;
import mma.admin.domains.interfaces.AvailabilitySearchStrategy;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SimpleAvailabilitySearch implements AvailabilitySearchStrategy {

//    @Override
//    public List<Availability> searchAvailabilities(List<Agenda> calendars, TimeSlot workingHours, LocalDate date, Duration duration, int roomCapacity) {
//        List<Availability> availabilities = new ArrayList<>();
//        LocalDate end = date.plusDays(2);
//        LocalDate currentDate;
//        for (Agenda agenda : calendars) {
//            if (agenda.getRoom().isValid(roomCapacity)) {
//                currentDate = date;
//                while (date.isBefore(end) || date.isEqual(end)) {
//                    //ajoute une liste de disponibilité par date
//                    agenda.searchAvailabilities(workingHours,date, duration, roomCapacity);
//                    currentDate = currentDate.plusDays(1);
//                }
//                availabilities.addAll(agenda.getAvailabilities());
//            }
//        }
//
//        return availabilities;
//    }

    @Override
    public List<Availability> searchAvailabilities(Agenda agenda, TimeSlot workingHours, LocalDate date, Duration duration, int roomCapacity) {
        LocalDate end = date.plusDays(2);
        LocalDate currentDate = date;

        while (!currentDate.isAfter(end)) {
            agenda.searchAvailabilities(workingHours,date, currentDate, duration, roomCapacity);
            currentDate = currentDate.plusDays(1);
        }
        return agenda.getAvailabilities().stream().toList();
    }
}
