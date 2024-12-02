package mma.admin.domains;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationPrestationTest {

    @Test
    void should_create_instance_when_valid_reservation_id_and_service_id_are_provided() {
        // Arrange
        int reservationId = 1;
        int serviceId = 2;

        // Act
        ReservationPrestation reservationPrestation = new ReservationPrestation(reservationId, serviceId);

        // Assert
        assertEquals(reservationId, reservationPrestation.reservationId());
        assertEquals(serviceId, reservationPrestation.serviceId());
    }

    @Test
    void should_throw_exception_when_reservation_id_is_non_positive() {
        // Arrange
        int invalidReservationId = 0;
        int serviceId = 2;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new ReservationPrestation(invalidReservationId, serviceId));
        assertEquals("L'identifiant de la réservation doit être positif.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_service_id_is_non_positive() {
        // Arrange
        int reservationId = 1;
        int invalidServiceId = -3;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new ReservationPrestation(reservationId, invalidServiceId));
        assertEquals("L'identifiant de la prestation doit être positif.", exception.getMessage());
    }

    @Test
    void it_should_return_correct_string_representation_when_to_string_is_called() {
        // Arrange
        int reservationId = 10;
        int serviceId = 20;
        ReservationPrestation reservationPrestation = new ReservationPrestation(reservationId, serviceId);

        // Act
        String result = reservationPrestation.toString();

        // Assert
        assertEquals("ReservationPrestation{reservationId=10, serviceId=20}", result);
    }

    @Test
    void given_valid_ids_when_instance_is_created_then_fields_should_match_input_values() {
        // Arrange
        int reservationId = 100;
        int serviceId = 200;

        // Act
        ReservationPrestation reservationPrestation = new ReservationPrestation(reservationId, serviceId);

        // Assert
        assertEquals(100, reservationPrestation.reservationId());
        assertEquals(200, reservationPrestation.serviceId());
    }
}

