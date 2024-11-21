package mma.admin.infrastructures;

import mma.admin.domains.Agenda;
import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.CalendarsWriter;
import mma.admin.domains.interfaces.ReservationsService;
import mma.admin.domains.interfaces.RoomsReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBCalendarsWriter implements CalendarsWriter {
    private ReservationsService reservationsService;

    public DBCalendarsWriter(ReservationsService reservationsService) {
        this.reservationsService = reservationsService;
    }

    public void addReservations(Reservation reservation) {
        reservationsService.addReservation(reservation);
    }
}
