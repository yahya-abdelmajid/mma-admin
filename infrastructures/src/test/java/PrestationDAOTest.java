import mma.admin.domains.Prestation;
import mma.admin.domains.ReservationPrestation;
import mma.admin.infrastructures.dao.PrestationDAOImpl;
import mma.admin.infrastructures.service.DBReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrestationDAOTest {

    DataBaseInitializer db;
    DBReservationService reservationService; // tu l'utilisais via l'interface, ici on garde l'impl directe

    @BeforeEach
    void setUp() throws SQLException {
        db = new DataBaseInitializer();
        db.createTable();

        db.addServiceExample();

        reservationService = new DBReservationService(db.getSQLDataStorage());
    }

    @AfterEach
    void cleanup() throws SQLException {
        db.clearTable();
        db.connection.close();
    }

    @Test
    void test_select_prestation() {
        List<Prestation> prestations = reservationService.getAllPrestations();
        assertEquals(prestations.get(0), new Prestation(1, "Café et boissons"));
    }

    @Test
    void test_select_prestation_vide() throws SQLException {
        // Arrange: nouvelle base sans données dans "service"
        DataBaseInitializer db2 = new DataBaseInitializer();
        try {
            db.clearTable();
            db2.createTable();
            PrestationDAOImpl dao = new PrestationDAOImpl(db2.connection);

            // Act
            List<Prestation> result = dao.findAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty(), "La liste des prestations devrait être vide");
        } finally {
            db2.clearTable();
            db2.connection.close();
        }
    }

    @Test
    void test_select_reservation_service_vide() {
        // Arrange
        PrestationDAOImpl dao = new PrestationDAOImpl(db.connection);

        // Act
        List<ReservationPrestation> links = dao.findAllReservationService();

        // Assert
        assertNotNull(links);
        assertTrue(links.isEmpty(), "La table de jointure devrait être vide au départ");
    }

    @Test
    void test_insert_reservation_service_puis_select() {
        // Arrange
        PrestationDAOImpl dao = new PrestationDAOImpl(db.connection);

        // Act
        dao.insert(1, 2); // reservation_id = 1, service_id = 2
        dao.insert(1, 3); // autre service pour la même réservation
        List<ReservationPrestation> links = dao.findAllReservationService();

        // Assert
        assertEquals(2, links.size());
        // On vérifie qu'on retrouve (1,2) et (1,3)
        assertTrue(
                links.stream().anyMatch(l -> l.reservationId() == 1 && l.serviceId() == 2),
                "Lien (1,2) attendu"
        );
        assertTrue(
                links.stream().anyMatch(l -> l.reservationId() == 1 && l.serviceId() == 3),
                "Lien (1,3) attendu"
        );
    }

    @Test
    void test_select_prestation_exception() throws Exception {
        // Arrange: connexion fermée pour provoquer une SQLException -> RuntimeException attendue
        DataBaseInitializer db2 = new DataBaseInitializer();
        db2.createTable();
        db2.connection.close(); // ferme la connexion
        PrestationDAOImpl dao = new PrestationDAOImpl(db2.connection);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, dao::findAll);
        assertTrue(ex.getMessage().contains("Database error while fetching all Prestations"));
        // pas besoin de finally: connexion déjà fermée, pas de data à nettoyer
    }

    @Test
    void test_select_reservation_service_exception() throws Exception {
        // Arrange: connexion fermée pour provoquer une SQLException -> RuntimeException attendue
        DataBaseInitializer db2 = new DataBaseInitializer();
        db2.createTable();
        db2.connection.close();
        PrestationDAOImpl dao = new PrestationDAOImpl(db2.connection);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, dao::findAllReservationService);
        assertTrue(ex.getMessage().contains("Database error while fetching reservations"));
    }
}
