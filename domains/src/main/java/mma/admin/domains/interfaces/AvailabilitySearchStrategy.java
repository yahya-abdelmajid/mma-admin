package mma.admin.domains.interfaces;

import mma.admin.domains.Agenda;
import mma.admin.domains.Availability;
import mma.admin.domains.TimeSlot;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilitySearchStrategy {
    List<Availability> searchAvailabilities(Agenda agenda, TimeSlot workingHours, LocalDate date, Duration duration, int roomCapacity) ;
}
