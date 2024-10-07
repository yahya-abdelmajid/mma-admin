package mma.admin.domains.interfaces;

import mma.admin.domains.RoomId;

import java.util.stream.Stream;

public interface RoomsReader {
    Stream<RoomId> getAllIds();

}
