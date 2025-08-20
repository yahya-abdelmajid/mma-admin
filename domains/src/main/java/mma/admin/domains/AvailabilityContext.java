package mma.admin.domains;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

record AvailabilityContext(LocalDate date, LocalDate currentDate, Duration duration, int capacity,
                           Set<Reservation> reservations) {
    public AvailabilityContext {
        if (date == null || currentDate == null || duration == null || capacity <= 0 || reservations == null) {
            throw new IllegalArgumentException("Arguments invalides pour AvailabilityContext");
        }
    }
}


