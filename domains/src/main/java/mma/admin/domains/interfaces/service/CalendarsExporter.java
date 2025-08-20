package mma.admin.domains.interfaces.service;

import mma.admin.domains.Reservation;
import mma.admin.domains.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CalendarsExporter {
    void createIcsFileIfNotExist() throws IOException;
    void registerInCalendar(String path, List<Reservation> reservation) throws IOException, ParseException;
}
