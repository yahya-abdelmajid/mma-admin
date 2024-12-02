package mma.admin.infrastructures;

import mma.admin.domains.Agenda;
import mma.admin.domains.Room;

public class AgendaFactory {
    public static Agenda createAgenda(Room room) {
        return new Agenda(room);
    }
}
