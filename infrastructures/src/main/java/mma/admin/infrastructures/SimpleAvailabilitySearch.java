package mma.admin.infrastructures;

import mma.admin.domains.Agenda;
import mma.admin.domains.Availability;
import mma.admin.domains.TimeSlot;
import mma.admin.domains.interfaces.AvailabilitySearchStrategy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class SimpleAvailabilitySearch implements AvailabilitySearchStrategy {

    @Override
    public List<Availability> searchAvailabilities(Agenda agenda, TimeSlot workingHours, LocalDate date, TemporalAmount duration, int roomCapacity) {
        LocalDate end = date.plusDays(2);
        LocalDate currentDate = date;

        while (!currentDate.isAfter(end)) {
            agenda.searchAvailabilities(workingHours,date, currentDate, (Duration)duration, roomCapacity);
            currentDate = currentDate.plusDays(1);
        }
        return agenda.getAvailabilities().stream().toList();
    }
}
