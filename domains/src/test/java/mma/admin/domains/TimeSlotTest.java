package mma.admin.domains;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    // ---------- Constructeurs ----------

    @Test
    void givenNullParamsOnDayStartEndCtor_whenConstruct_thenThrowsNpeWithMessage() {
        // Arrange
        LocalDate d = LocalDate.now();
        LocalTime s = LocalTime.NOON;
        LocalTime e = LocalTime.MIDNIGHT;

        // Act & Assert
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> new TimeSlot(null, s, e));
        assertEquals("Les paramètres day, start et end ne doivent pas être null.", ex1.getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> new TimeSlot(d, null, e));
        assertEquals("Les paramètres day, start et end ne doivent pas être null.", ex2.getMessage());

        NullPointerException ex3 = assertThrows(NullPointerException.class, () -> new TimeSlot(d, s, null));
        assertEquals("Les paramètres day, start et end ne doivent pas être null.", ex3.getMessage());

        NullPointerException ex4 = assertThrows(NullPointerException.class, () -> new TimeSlot(null, null, null));
        assertEquals("Les paramètres day, start et end ne doivent pas être null.", ex4.getMessage());
    }

    @Test
    void givenNullParamsOnTodayCtor_whenConstruct_thenThrowsNpeWithMessage() {
        // Arrange
        LocalTime s = LocalTime.NOON;

        // Act & Assert
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> new TimeSlot(null, LocalTime.MIDNIGHT));
        assertEquals("Les paramètres start et end ne doivent pas être null.", ex1.getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> new TimeSlot(s, null));
        assertEquals("Les paramètres start et end ne doivent pas être null.", ex2.getMessage());
    }

    @Test
    void givenNullParamsOnDateTimeCtor_whenConstruct_thenThrowsNpeWithMessage() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> new TimeSlot(null, now));
        assertEquals("Les paramètres start et end ne doivent pas être null.", ex1.getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> new TimeSlot(now, null));
        assertEquals("Les paramètres start et end ne doivent pas être null.", ex2.getMessage());
    }

    @Test
    void givenEquivalentInstants_whenConstructViaDifferentCtors_thenFieldsConsistentAndEqualsTrue() {
        // Arrange
        LocalDate d = LocalDate.now().plusDays(1);
        LocalTime s = LocalTime.of(9, 0);
        LocalTime e = LocalTime.of(17, 0);

        // Act
        TimeSlot a = new TimeSlot(d, s, e);
        TimeSlot b = new TimeSlot(LocalDateTime.of(d, s), LocalDateTime.of(d, e));
        TimeSlot c = new TimeSlot(s, e); // today

        // Assert
        assertEquals(d, a.getDay());
        assertEquals(s, a.getStart());
        assertEquals(e, a.getEnd());
        assertEquals(LocalDateTime.of(d, s), a.getStartTime());
        assertEquals(LocalDateTime.of(d, e), a.getEndTime());
        assertEquals(a, b);        // même jour/heure
        assertNotEquals(a, c);     // day (today) ≠ d
    }

    // ---------- Accesseurs & toString ----------

    @Test
    void givenSlot_whenGettersAndToString_thenReturnExpectedValues() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 1, 2);
        LocalTime s = LocalTime.of(8, 15);
        LocalTime e = LocalTime.of(11, 45);
        TimeSlot ts = new TimeSlot(d, s, e);

        // Act
        String repr = ts.toString();

        // Assert
        assertEquals(d, ts.getDay());
        assertEquals(s, ts.getStart());
        assertEquals(e, ts.getEnd());
        assertEquals(LocalDateTime.of(d, s), ts.getStartTime());
        assertEquals(LocalDateTime.of(d, e), ts.getEndTime());
        assertEquals(d + " : " + s + " - " + e, repr);
    }

    // ---------- isNotInPast ----------

    @Test
    void givenFutureStart_whenIsNotInPast_thenTrue() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusMinutes(2);
        LocalDateTime end = start.plusHours(1);
        TimeSlot ts = new TimeSlot(start, end);

        // Act
        boolean result = ts.isNotInPast();

        // Assert
        assertTrue(result);
    }

    @Test
    void givenPastStart_whenIsNotInPast_thenFalse() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusMinutes(2);
        LocalDateTime end = start.plusHours(1);
        TimeSlot ts = new TimeSlot(start, end);

        // Act
        boolean result = ts.isNotInPast();

        // Assert
        assertFalse(result);
    }

    // ---------- isWithinWorkingHours ----------

    @Test
    void givenNullWorkingHours_whenIsWithinWorkingHours_thenThrowsNpeWithMessage() {
        // Arrange
        TimeSlot ts = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(17, 0));

        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () -> ts.isWithinWorkingHours(null));

        // Assert
        assertEquals("Le creneau horaire ne peut pas être null.", ex.getMessage());
    }

    @Test
    void givenInsideBounds_whenIsWithinWorkingHours_thenReturnsTrue() {
        // Arrange (working: 8–18 ; slot: 9–17)
        TimeSlot working = new TimeSlot(LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0));
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(17, 0));

        // Act
        boolean result = slot.isWithinWorkingHours(working);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenStartBeforeWorkingButEndEqualsWorking_whenIsWithinWorkingHours_thenReturnsTrueDueToOrBranch() {
        // Arrange
        // Expression = ((start>=work.start) && end<work.end) || end==work.end
        TimeSlot working = new TimeSlot(LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0));
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(7, 0), LocalTime.of(18, 0));

        // Act
        boolean result = slot.isWithinWorkingHours(working);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenOutsideBounds_whenIsWithinWorkingHours_thenReturnsFalse() {
        // Arrange (slot 7–19 vs working 8–18)
        TimeSlot working = new TimeSlot(LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0));
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(7, 0), LocalTime.of(19, 0));

        // Act
        boolean result = slot.isWithinWorkingHours(working);

        // Assert
        assertFalse(result);
    }

    // ---------- isOverlay ----------

    @Test
    void givenNullArg_whenIsOverlay_thenThrowsNpeWithMessage() {
        // Arrange
        TimeSlot base = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));

        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () -> base.isOverlay(null));

        // Assert
        assertEquals("Le creneau horaire ne peut pas être null.", ex.getMessage());
    }

    @Test
    void givenOverlappingIntervals_whenIsOverlay_thenReturnsTrueBothWays() {
        // Arrange
        TimeSlot base = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot other = new TimeSlot(LocalDate.now(), LocalTime.of(11, 0), LocalTime.of(13, 0));

        // Act & Assert
        assertTrue(base.isOverlay(other));
        assertTrue(other.isOverlay(base));
    }

    @Test
    void givenTouchingAtBoundary_whenIsOverlay_thenReturnsFalse() {
        // Arrange (9–12) vs (12–13)
        TimeSlot base = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot touching = new TimeSlot(LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(13, 0));

        // Act & Assert
        assertFalse(base.isOverlay(touching));
        assertFalse(touching.isOverlay(base));
    }

    @Test
    void givenDisjointIntervals_whenIsOverlay_thenReturnsFalse() {
        // Arrange
        TimeSlot base = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot far = new TimeSlot(LocalDate.now(), LocalTime.of(13, 0), LocalTime.of(14, 0));

        // Act
        boolean result = base.isOverlay(far);

        // Assert
        assertFalse(result);
    }

    // ---------- isInTimeSlot (LocalTime) ----------

    @Test
    void givenNullTime_whenIsInTimeSlot_thenThrowsNpeWithMessage() {
        // Arrange
        TimeSlot base = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));

        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () -> base.isInTimeSlot(null));

        // Assert
        assertEquals("Le time ne peut pas être null.", ex.getMessage());
    }

    @Test
    void givenInsideAndBoundaries_whenIsInTimeSlot_thenReturnsTrueInclusiveBothEnds() {
        // Arrange
        TimeSlot base = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));

        // Act & Assert
        assertTrue(base.isInTimeSlot(LocalTime.of(10, 0)));  // inside
        assertTrue(base.isInTimeSlot(LocalTime.of(9, 0)));   // start inclusive
        assertTrue(base.isInTimeSlot(LocalTime.of(12, 0)));  // end inclusive (implémentation)
    }

    @Test
    void givenBeforeOrAfter_whenIsInTimeSlot_thenReturnsFalse() {
        // Arrange
        TimeSlot base = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));

        // Act & Assert
        assertFalse(base.isInTimeSlot(LocalTime.of(8, 59)));
        assertFalse(base.isInTimeSlot(LocalTime.of(12, 1)));
    }

    // ---------- isBeetween (sic) ----------

    @Test
    void givenNullStartOrEnd_whenIsBeetween_thenReturnsFalse() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 1, 10);
        TimeSlot ts = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act & Assert
        assertFalse(ts.isBeetween(null, d.plusDays(1)));
        assertFalse(ts.isBeetween(d.minusDays(1), null));
        assertFalse(ts.isBeetween(null, null));
    }

    @Test
    void givenInsideRange_whenIsBeetween_thenReturnsTrue() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 1, 10);
        TimeSlot ts = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));
        LocalDate start = d.minusDays(2);
        LocalDate end = d.plusDays(2);

        // Act
        boolean result = ts.isBeetween(start, end);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenOnBoundaries_whenIsBeetween_thenReturnsTrueInclusive() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 1, 10);
        TimeSlot ts = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act & Assert
        assertTrue(ts.isBeetween(d, d.plusDays(5)));       // start boundary
        assertTrue(ts.isBeetween(d.minusDays(5), d));      // end boundary
    }

    @Test
    void givenOutsideRange_whenIsBeetween_thenReturnsFalse() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 1, 10);
        TimeSlot ts = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act & Assert
        assertFalse(ts.isBeetween(d.plusDays(1), d.plusDays(5)));    // before start
        assertFalse(ts.isBeetween(d.minusDays(5), d.minusDays(1)));  // after end
    }

    // ---------- isAfter (LocalTime) ----------

    @Test
    void givenNullTime_whenIsAfter_thenThrowsNpe() {
        // Arrange
        TimeSlot ts = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> ts.isAfter(null));
    }

    @Test
    void givenTimes_whenIsAfter_thenStartInclusiveEndExclusive() {
        // Arrange
        TimeSlot ts = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));

        // Act & Assert
        assertFalse(ts.isAfter(LocalTime.of(8, 59))); // before start
        assertTrue(ts.isAfter(LocalTime.of(9, 0)));   // start inclusive
        assertTrue(ts.isAfter(LocalTime.of(10, 0)));  // inside
        assertFalse(ts.isAfter(LocalTime.of(12, 0))); // end exclusive
        assertFalse(ts.isAfter(LocalTime.of(12, 1))); // after end
    }

    // ---------- equals ----------

    @Test
    void givenSameInstance_whenEquals_thenReturnsTrue() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 1, 1);
        TimeSlot ts = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act
        boolean eq = ts.equals(ts);

        // Assert
        assertTrue(eq);
    }

    @Test
    void givenNullOrDifferentType_whenEquals_thenReturnsFalse() {
        // Arrange
        TimeSlot ts = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act & Assert
        assertFalse(ts.equals(null));
        assertFalse(ts.equals("not-a-timeslot"));
    }

    @Test
    void givenIdenticalFields_whenEquals_thenTrue_otherwiseFalse() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 1, 1);
        TimeSlot a = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot b = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot diffStart = new TimeSlot(d, LocalTime.of(8, 59), LocalTime.of(10, 0));
        TimeSlot diffEnd = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 1));
        TimeSlot diffDay = new TimeSlot(d.plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act & Assert
        assertTrue(a.equals(b));
        assertFalse(a.equals(diffStart));
        assertFalse(a.equals(diffEnd));
        assertFalse(a.equals(diffDay));
    }

    // ---------- compareTo ----------

    @Test
    void givenIdenticalInstants_whenCompareTo_thenReturnsZero() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 2, 2);
        TimeSlot a = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot b = new TimeSlot(LocalDateTime.of(d, LocalTime.of(9, 0)),
                LocalDateTime.of(d, LocalTime.of(10, 0)));

        // Act
        int cmp = a.compareTo(b);

        // Assert
        assertEquals(0, cmp);
    }

    @Test
    void givenDifferentDays_whenCompareTo_thenOrdersByDayStartEndSum() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 3, 3);
        TimeSlot base = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot before = new TimeSlot(d.minusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot after = new TimeSlot(d.plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act & Assert
        assertTrue(base.compareTo(before) > 0);
        assertTrue(base.compareTo(after) < 0);
    }

    @Test
    void givenSameDayDifferentTimes_whenCompareTo_thenEarlierStartsAreSmaller() {
        // Arrange
        LocalDate d = LocalDate.of(2030, 3, 3);
        TimeSlot base = new TimeSlot(d, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot earlier = new TimeSlot(d, LocalTime.of(8, 0), LocalTime.of(9, 0));
        TimeSlot later = new TimeSlot(d, LocalTime.of(10, 0), LocalTime.of(11, 0));

        // Act & Assert
        assertTrue(base.compareTo(earlier) > 0);
        assertTrue(base.compareTo(later) < 0);
    }
}
