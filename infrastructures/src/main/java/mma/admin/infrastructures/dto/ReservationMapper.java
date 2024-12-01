package mma.admin.infrastructures.dto;

import mma.admin.domains.Reservation;
import mma.admin.infrastructures.ReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    ReservationDTO reservationToReservationDTO(Reservation reservation);
}
