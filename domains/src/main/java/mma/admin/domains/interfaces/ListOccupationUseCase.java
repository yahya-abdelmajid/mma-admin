package mma.admin.domains.interfaces;

import mma.admin.domains.Occupation;
import mma.admin.domains.RoomId;

import java.util.stream.Stream;

public interface ListOccupationUseCase {
    Stream<RoomId> getRoomsIds();
    Stream<Occupation> getOccupationForRoom (RoomId id);
}
