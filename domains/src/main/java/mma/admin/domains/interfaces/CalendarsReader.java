package mma.admin.domains.interfaces;

import mma.admin.domains.Agenda;
import mma.admin.domains.Reservation;
import mma.admin.domains.RoomId;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CalendarsReader {
    Map<RoomId, Agenda> getCalendars();
    void readAllReservations();
}
