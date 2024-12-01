package mma.admin.infrastructures;

import mma.admin.domains.RoomId;
import mma.admin.domains.TimeSlot;

public class ReservationDTO {
    private RoomId roomId;
    private TimeSlot timeSlot;

    public ReservationDTO() {
    }

    public ReservationDTO(String roomId, TimeSlot timeSlot) {
        this.roomId = new RoomId(roomId);
        this.timeSlot = timeSlot;
    }

    public RoomId getRoomId() {
        return roomId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

}
