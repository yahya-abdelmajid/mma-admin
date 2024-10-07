package mma.admin.domains.interfaces;

import mma.admin.domains.Occupation;
import mma.admin.domains.RoomId;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface OccupationReader {
    Stream<Occupation> getOccupations(RoomId id, LocalDate date);

}
