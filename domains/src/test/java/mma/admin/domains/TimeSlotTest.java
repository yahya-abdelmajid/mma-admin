package mma.admin.domains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

//Classe de test généré partiellement par l'IA
class TimeSlotTest {

    private TimeSlot timeSlot;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setup() {
        date = LocalDate.now().plusDays(1); // Utilisation d'une date future pour tester is_not_in_past
        start = LocalTime.of(9, 0);
        end = LocalTime.of(17, 0);
        startTime = LocalDateTime.of(date, start);
        endTime = LocalDateTime.of(date, end);
        timeSlot = new TimeSlot(date, start, end);
    }

    @Test
    void get_start_should_return_correct_start() {
        assertEquals(start, timeSlot.getStart());
    }

    @Test
    void get_end_should_return_correct_end() {
        assertEquals(end, timeSlot.getEnd());
    }

    @Test
    void get_day_should_return_correct_day() {
        assertEquals(date, timeSlot.getDay());
    }

    @Test
    void get_start_time_should_return_correct_start_time() {
        assertEquals(startTime, timeSlot.getStartTime());
    }

    @Test
    void get_end_time_should_return_correct_end_time() {
        assertEquals(endTime, timeSlot.getEndTime());
    }

    @Test
    void is_not_in_past_should_return_true_for_future_time_slot() {
        assertTrue(timeSlot.isNotInPast());
    }

    @Test
    void is_not_in_past_should_return_false_for_past_time_slot() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        TimeSlot pastTimeSlot = new TimeSlot(pastDate, start, end);
        assertFalse(pastTimeSlot.isNotInPast());
    }

    @Test
    void is_not_in_past_should_throw_exception_for_null_date() {
        assertThrows(NullPointerException.class, () -> new TimeSlot(null, start, end));
    }

    @Test
    void is_within_working_hours_should_return_false_when_slot_is_outside_hours() {
        TimeSlot outsideSlot = new TimeSlot(LocalTime.of(7, 0), LocalTime.of(18, 0));

        assertFalse(outsideSlot.isWithinWorkingHours(timeSlot));
    }

    @Test
    void is_within_working_hours_should_return_true_when_slot_matches_exactly() {
        assertTrue(timeSlot.isWithinWorkingHours(new TimeSlot(start, end)));
    }

    @Test
    void is_within_working_hours_should_return_false_when_slot_overlaps_partially() {
        TimeSlot overlappingSlot = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(10, 0));
        assertFalse(timeSlot.isWithinWorkingHours(overlappingSlot));
    }

    @Test
    void is_in_time_slot_should_return_true_when_time_is_within_slot() {
        LocalTime middleTime = LocalTime.of(12, 0);
        assertTrue(timeSlot.isInTimeSlot(middleTime));
    }

    @Test
    void is_in_time_slot_should_return_false_when_time_is_outside_slot() {
        LocalTime outsideTime = LocalTime.of(18, 0);
        assertFalse(timeSlot.isInTimeSlot(outsideTime));
    }

    @Test
    void is_in_time_slot_should_throw_exception_for_null_time() {
        assertThrows(NullPointerException.class, () -> timeSlot.isInTimeSlot(null));
    }

    @Test
    void equals_should_return_false_when_other_is_null() {
        assertNotEquals(timeSlot, null);
    }

    @Test
    void equals_should_return_false_when_other_is_different_class() {
        assertNotEquals(timeSlot, "not a TimeSlot");
    }

    @Test
    void hash_code_should_be_consistent_with_equals() {
        TimeSlot identicalTimeSlot = new TimeSlot(date, start, end);
        assertEquals(timeSlot.hashCode(), identicalTimeSlot.hashCode());
    }

    @Test
    void to_string_should_include_day_start_and_end() {
        String expected = date + " : " + start + " - " + end;
        assertEquals(expected, timeSlot.toString());
    }
}
