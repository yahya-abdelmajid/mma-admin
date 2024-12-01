package mma.admin.domains;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Les Tests on étaient développé via l'aide d'une IA
public class AvailabilityTest {

        @Test
        void should_return_room_id_when_get_room_is_called() {
            // Arrange
            RoomId roomId = new RoomId("R101");
            TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0));
            Availability availability = new Availability(roomId, timeSlot);

            // Act
            RoomId result = availability.getRoom();

            // Assert
            assertEquals(roomId, result);
        }

        @Test
        void should_return_time_slot_when_get_time_slot_is_called() {
            // Arrange
            RoomId roomId = new RoomId("R101");
            TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0));
            Availability availability = new Availability(roomId, timeSlot);

            // Act
            TimeSlot result = availability.getTimeSlot();

            // Assert
            assertEquals(timeSlot, result);
        }

        @Test
        void should_calculate_score_correctly_based_on_date_duration_and_capacity() {
            // Arrange
            RoomId roomId = new RoomId("R101");
            TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(11, 0));
            Availability availability = new Availability(roomId, timeSlot);
            Room room = new Room(roomId,"salle 1", 20);
            LocalDate date = LocalDate.now().plusDays(1);
            Duration duration = Duration.ofHours(2);
            int capacity = 15;

            // Act
            availability.calculatorScore(date, room, duration, capacity);
            // Assert
            int expectedScore = (100 - 20) + (100 - 12) + (100 - 5); // 257
            assertEquals(expectedScore, availability.getScore());
        }

        @Test
        void it_should_compare_two_availabilities_based_on_score() {
            // Arrange
            RoomId roomId1 = new RoomId("R101");
            RoomId roomId2 = new RoomId("R102");
            TimeSlot timeSlot1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(11, 0));
            TimeSlot timeSlot2 = new TimeSlot(LocalTime.of(12, 0), LocalTime.of(14, 0));
            Availability availability1 = new Availability(roomId1, timeSlot1);
            Availability availability2 = new Availability(roomId1, timeSlot2);

            Room room = new Room(roomId1,"Salle 1", 20);
            LocalDate date = LocalDate.now();
            Duration duration = Duration.ofHours(2);
            int capacity = 15;

            availability1.calculatorScore(date, room, duration, capacity);
            availability2.calculatorScore(date, room, duration, capacity - 5); // Lower capacity
            // Act
            int comparisonResult = availability1.compareTo(availability2);

            // Assert
            assertTrue(comparisonResult > 0);
        }

        @Test
        void given_two_availabilities_when_compare_then_should_return_based_on_scores() {
            // Arrange
            RoomId roomId1 = new RoomId("R101");
            RoomId roomId2 = new RoomId("R102");
            TimeSlot timeSlot1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(11, 0));
            TimeSlot timeSlot2 = new TimeSlot(LocalTime.of(12, 0), LocalTime.of(14, 0));
            Availability availability1 = new Availability(roomId1, timeSlot1);
            Availability availability2 = new Availability(roomId2, timeSlot2);

            Room room = new Room(roomId1,"Salle 1", 20);
            LocalDate date = LocalDate.now();
            Duration duration = Duration.ofHours(2);
            int capacity = 15;

            availability1.calculatorScore(date, room, duration, capacity);
            availability2.calculatorScore(date, room, duration, capacity - 5); // Lower capacity

            // Act
            int comparisonResult = availability1.compare(availability1, availability2);

            // Assert
            assertTrue(comparisonResult < 0); // availability1 is considered smaller (higher score comes first)
        }

        @Test
        void should_return_correct_string_representation() {
            // Arrange
            RoomId roomId = new RoomId("R101");
            TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0));
            Availability availability = new Availability(roomId, timeSlot);

            // Act
            String result = availability.toString();

            // Assert
            assertEquals("R1012024-12-01 : 09:00 - 10:00", result);
        }
}
