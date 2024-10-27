/*
 * This source file is an example
 */
package mma.admin.views;

import mma.admin.presentations.Presenter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class View {
    Presenter presenter;

    public View(Presenter presenter) {
        this.presenter = presenter;
    }

    public void displayMenu() throws IOException, ParseException {
        presenter.displayMenu();
    }


    public void displayGrid(LocalDate date) {
        presenter.displayAvailability(date);
    }

    public void searchReservation(String day,String roomId,String start,String end) {
        presenter.displayReservationIfExist(day,roomId,start,end);
    }



    /*
    private static final Logger LOGGER = LogManager.getLogger();
    public static void greets() {
        LOGGER.info("Bonjour de la vue");
    }*/
}
