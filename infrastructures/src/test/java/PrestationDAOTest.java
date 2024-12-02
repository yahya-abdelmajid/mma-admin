import mma.admin.domains.Prestation;
import mma.admin.domains.interfaces.service.PrestationService;
import mma.admin.domains.interfaces.service.ReservationService;
import mma.admin.infrastructures.service.DBReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrestationDAOTest {


    DataBaseInitializer db;
    ReservationService reservationService;

    @BeforeEach
    void setUp() throws SQLException {
       db = new DataBaseInitializer();
       db.clearTable();
       db.createTable();

        List<Prestation> prestations = List.of(
                new Prestation(1, "Café et boissons"),
                new Prestation(2, "Nourriture"),
                new Prestation(3, "Projecteur"),
                new Prestation(4, "Matériel sonore")
        );
        // Ajout des utilisateurs
        db.addPrestation(prestations);
        reservationService= new DBReservationService(db.getSQLDataStorage());
    }

    @Test
    void test_select_prestation() {
        //SQLiteOperations.insertData("Charlie", 28);
        // Ajouter des assertions pour vérifier les données.

        List<Prestation> prestations=reservationService.getAllPrestations();

        assertEquals(prestations.get(0),  new Prestation(1, "Café et boissons"));
    }

    @AfterEach
    void cleanup() throws SQLException {
        db.clearTable();
        db.connection.close();
    }
}
