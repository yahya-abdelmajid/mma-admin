package mma.admin.domains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    private TimeSlot timeSlot;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setup() {
        date = LocalDate.now().plusDays(1);
        start = LocalTime.of(9, 0);
        end = LocalTime.of(17, 0);
        startTime = LocalDateTime.of(date, start);
        endTime = LocalDateTime.of(date, end);
        timeSlot = new TimeSlot(date, start, end);
    }

    @Test
    void constructor_should_throw_exception_when_parameters_are_null() {
        assertThrows(NullPointerException.class, () -> new TimeSlot(null, start, end));
        assertThrows(NullPointerException.class, () -> new TimeSlot(date, null, end));
        assertThrows(NullPointerException.class, () -> new TimeSlot(date, start, null));
        assertThrows(NullPointerException.class, () -> new TimeSlot(null, null, null));
    }

    @Test
    void is_overlay_should_return_true_for_overlapping_slots() {
        TimeSlot overlappingSlot = new TimeSlot(date, LocalTime.of(16, 0), LocalTime.of(18, 0));
        assertTrue(timeSlot.isOverlay(overlappingSlot));
    }

    @Test
    void should_throw_exception_when_is_within_working_hours_is_null() {
        // Arrange
        TimeSlot timeSlot = new TimeSlot(date, LocalTime.of(16, 0), LocalTime.of(18, 0));


        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> timeSlot.isWithinWorkingHours(null));
        assertEquals("Le creneau horaire ne peut pas être null.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_is_in_time_slot_is_null() {
        // Arrange
        TimeSlot timeSlot = new TimeSlot(date, LocalTime.of(16, 0), LocalTime.of(18, 0));


        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> timeSlot.isInTimeSlot(null));
        assertEquals("Le time ne peut pas être null.", exception.getMessage());
    }

    @Test
    void is_overlay_should_return_false_for_non_overlapping_slots() {
        TimeSlot nonOverlappingSlot = new TimeSlot(date, LocalTime.of(18, 0), LocalTime.of(20, 0));
        assertFalse(timeSlot.isOverlay(nonOverlappingSlot));
    }

    @Test
    void equals_should_return_true_for_identical_slots() {
        TimeSlot identicalTimeSlot = new TimeSlot(date, start, end);
        assertEquals(timeSlot, identicalTimeSlot);
    }

    @Test
    void compare_to_should_return_zero_for_identical_slots() {
        TimeSlot identicalTimeSlot = new TimeSlot(startTime, endTime);
        assertEquals(0, timeSlot.compareTo(identicalTimeSlot));
    }

    @Test
    void compare_to_should_handle_different_days() {
        TimeSlot earlierSlot = new TimeSlot(date.minusDays(1), start, end);
        assertTrue(timeSlot.compareTo(earlierSlot) > 0);

        TimeSlot laterSlot = new TimeSlot(date.plusDays(1), start, end);
        assertTrue(timeSlot.compareTo(laterSlot) < 0);
    }

    @Test
    void compare_to_should_handle_different_times() {
        TimeSlot earlierStartTimeSlot = new TimeSlot(date, LocalTime.of(8, 0), end);
        assertTrue(timeSlot.compareTo(earlierStartTimeSlot) > 0);

        TimeSlot laterStartTimeSlot = new TimeSlot(date, LocalTime.of(10, 0), end);
        assertTrue(timeSlot.compareTo(laterStartTimeSlot) < 0);
    }
}
