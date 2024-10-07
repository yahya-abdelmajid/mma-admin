package mma.admin.domains;

import mma.admin.domains.interfaces.ListOccupationUseCase;
import mma.admin.domains.interfaces.OccupationReader;
import mma.admin.domains.interfaces.RoomsReader;

import java.io.Reader;
import java.util.stream.Stream;

public class ListOccupationImpl implements ListOccupationUseCase {
    RoomsReader roomsReader;
    OccupationReader occupationReader;

    public ListOccupationImpl(RoomsReader roomsReader, OccupationReader occupationReader) {
        this.roomsReader=roomsReader;
        this.occupationReader=occupationReader;
    }

    @Override
    public Stream<RoomId> getRoomsIds() {
        return Stream.empty();
    }

    @Override
    public Stream<Occupation> getOccupationForRoom(RoomId id) {
        return Stream.empty();
    }
}
