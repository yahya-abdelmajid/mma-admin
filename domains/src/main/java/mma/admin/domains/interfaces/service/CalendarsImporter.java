package mma.admin.domains.interfaces.service;

import mma.admin.domains.Reservation;

import java.util.List;

public interface CalendarsImporter {
    List<Reservation> readReservations(String path);
}
