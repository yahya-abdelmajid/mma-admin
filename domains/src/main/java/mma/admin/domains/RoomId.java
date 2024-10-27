package mma.admin.domains;

import java.util.Objects;

public class RoomId {

    public String id;

    public RoomId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomId that = (RoomId) o;

        return Objects.equals(id, that.getId());
    }


}
