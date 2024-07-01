package roomescape.domain.reservation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Reservation의 Date와 Theme의 Id(PK)를 기준으로 조회
     */
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
