package mma.admin.presentations;

import java.time.LocalDateTime;

public record ReservationT(
        String roomId,
        String matricule,
        LocalDateTime start,
        LocalDateTime end,
        String description,
        int expected,
        String services
) {}
