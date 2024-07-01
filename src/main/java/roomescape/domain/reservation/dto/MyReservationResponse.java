package roomescape.domain.reservation.dto;

import roomescape.domain.reservation.entity.Reservation;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {

    private static final String RESERVED = "예약";

    public static MyReservationResponse from(final Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTime(),
                RESERVED
        );
    }
}
