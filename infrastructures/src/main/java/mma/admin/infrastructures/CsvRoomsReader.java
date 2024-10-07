package mma.admin.infrastructures;

import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.RoomsReader;

import java.nio.file.Path;
import java.util.stream.Stream;

public class CsvRoomsReader implements RoomsReader {
    Path file;
    public CsvRoomsReader(Path path){
        this.file = path;
    }

    @Override
    public Stream<RoomId> getAllIds() {
        return Stream.empty();
    }
}
