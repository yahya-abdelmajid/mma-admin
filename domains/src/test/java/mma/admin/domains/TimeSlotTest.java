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
        LocalDateTime pastStartTime = LocalDateTime.of(pastDate, start);
        LocalDateTime pastEndTime = LocalDateTime.of(pastDate, end);
        timeSlot = new TimeSlot(pastStartTime, pastEndTime);

        assertFalse(timeSlot.isNotInPast());

    }

    @Test
    void is_within_working_hours_should_return_true_when_slot_exactly_matches_hours() {
        assertTrue(timeSlot.isWithinWorkingHours(start, end));
    }

    @Test
    void is_within_working_hours_should_return_false_when_start_is_before_working_hours() {
        LocalTime workingStart = LocalTime.of(10, 0);
        LocalTime workingEnd = LocalTime.of(18, 0);
        assertFalse(timeSlot.isWithinWorkingHours(workingStart, workingEnd));
    }

    @Test
    void is_within_working_hours_should_return_false_when_end_is_after_working_hours() {
        LocalTime workingStart = LocalTime.of(8, 0);
        LocalTime workingEnd = LocalTime.of(16, 0);
        assertFalse(timeSlot.isWithinWorkingHours(workingStart, workingEnd));
    }

    @Test
    void is_within_working_hours_should_return_true_when_slot_is_fully_within_hours() {
        LocalTime workingStart = LocalTime.of(8, 0);
        LocalTime workingEnd = LocalTime.of(18, 0);
        assertTrue(timeSlot.isWithinWorkingHours(workingStart, workingEnd));
    }

    @Test
    void is_in_time_slot_should_return_true_when_time_is_exactly_start_time() {
        assertTrue(timeSlot.isInTimeSlot(start));
    }

    @Test
    void is_in_time_slot_should_return_true_when_time_is_exactly_end_time() {
        assertTrue(timeSlot.isInTimeSlot(end));
    }

    @Test
    void is_in_time_slot_should_return_false_when_time_is_before_start_time() {
        LocalTime timeBeforeSlot = LocalTime.of(8, 0);
        assertFalse(timeSlot.isInTimeSlot(timeBeforeSlot));
    }

    @Test
    void is_in_time_slot_should_return_false_when_time_is_after_end_time() {
        LocalTime timeAfterSlot = LocalTime.of(18, 0);
        assertFalse(timeSlot.isInTimeSlot(timeAfterSlot));
    }

    @Test
    void to_string_should_return_correct_string_representation() {
        String expected = date + " : " + start + " - " + end;
        assertEquals(expected, timeSlot.toString());
    }

    @Test
    void equals_should_return_true_for_identical_time_slots() {
        TimeSlot identicalTimeSlot = new TimeSlot(date, start, end);
        assertEquals(timeSlot, identicalTimeSlot);
    }

    @Test
    void equals_should_return_false_for_different_time_slots() {
        TimeSlot differentTimeSlot = new TimeSlot(date, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertNotEquals(timeSlot, differentTimeSlot);
    }

    @Test
    void hash_code_should_return_same_hash_code_for_identical_time_slots() {
        TimeSlot identicalTimeSlot = new TimeSlot(date, start, end);
        assertEquals(timeSlot.hashCode(), identicalTimeSlot.hashCode());
    }

    @Test
    void hash_code_should_return_different_hash_code_for_different_time_slots() {
        TimeSlot differentTimeSlot = new TimeSlot(date, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertNotEquals(timeSlot.hashCode(), differentTimeSlot.hashCode());
    }
}
