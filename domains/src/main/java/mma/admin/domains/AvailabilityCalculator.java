package mma.admin.domains;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class AvailabilityCalculator {
    private final Room room;

    public AvailabilityCalculator(Room room) {
        this.room = room;
    }


    public Set<Availability> calculate(TimeSlot wh, AvailabilityContext ctx) {
        Set<Availability> results = new HashSet<>();
        SlotCursor cursor = new SlotCursor(wh.getStart());

        LocalTime end = wh.getEnd();

        while (!cursor.temp.isAfter(end)) {
            TimeSlot timeSlot = new TimeSlot(cursor.start, cursor.temp.plusMinutes(1));
            cursor.range = cursor.range.plusMinutes(30);
            cursor.temp = cursor.start.plus(cursor.range);

            if (checkAndAdd(timeSlot, end, cursor, ctx, results)) {
                cursor.range = Duration.ZERO;
            }
        }

        return results;
    }

    private boolean checkAndAdd(TimeSlot timeSlot, LocalTime end, SlotCursor cursor,
                                AvailabilityContext ctx, Set<Availability> results) {

        for (Reservation r : ctx.reservations()) {
            boolean isOverlay = r.isOverlay(timeSlot);

            if ((isOverlay || cursor.temp.isAfter(end)) && cursor.range.compareTo(ctx.duration()) >= 0) {
                results.add(createAvailability(
                        ctx.date(),
                        ctx.currentDate(),
                        cursor.start,
                        cursor.temp.minusMinutes(30),
                        ctx.capacity()
                ));
                return true;
            } else if (isOverlay) {
                cursor.start = r.getTimeSlot().getEnd();
                cursor.temp = cursor.start;
                return true;
            }
        }
        return false;
    }

    private Availability createAvailability(LocalDate date, LocalDate currentDate,
                                            LocalTime start, LocalTime end, int capacity) {
        TimeSlot slot = new TimeSlot(currentDate, start, end);
        Availability availability = new Availability(room.roomId(), slot);
        availability.calculatorScore(date, slot.getDay(), room, capacity);
        return availability;
    }

    private static class SlotCursor {
        private LocalTime start;
        private LocalTime temp;
        private Duration range;

        SlotCursor(LocalTime start) {
            this.start = start;
            this.temp = start;
            this.range = Duration.ZERO;
        }
    }
}
