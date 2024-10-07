package mma.admin.domains;

import mma.admin.domains.interfaces.RoomsReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ListOccupationImplTest {

    @Test
void should_read_all_rooms_from_the_reader(){
        RoomsReader roomsReader=new FakeRooms (RoomId.of('LC1'),RoomId.of("LC2"));
        var usecase= new ListOccupationImpl(roomsReader,null);
        Stream<RoomId> actual= usecase.getRoomsIds();
        assertIterableEquals(actual.toList(), List.of(RoomId.of("LC1"),RoomId.of("LC2")));

}

}