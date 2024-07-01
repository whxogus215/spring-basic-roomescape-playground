package roomescape.domain.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.domain.reservation.dto.MyReservationResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.member.dto.LoginMember;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final TimeRepository timeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest,
                                    LoginMember loginMember) {
        if (reservationRequest.getName() == null) {
            ReservationRequest newRequest = new ReservationRequest(
                    loginMember.name(),
                    reservationRequest.getDate(),
                    reservationRequest.getTime(),
                    reservationRequest.getTheme()
            );
            reservationRequest = newRequest;
        }

        final Time findTime = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new RuntimeException("요청한 시간이 존재하지 않습니다."));
        final Theme findTheme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new RuntimeException("요청한 테마가 존재하지 않습니다."));
        Reservation reservation = reservationRepository.save(
                reservationRequest.toEntity(findTime, findTheme));

        return new ReservationResponse(
                reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(),
                reservation.getDate(), reservation.getTime().getTime());
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(Long memberId) {
        return reservationRepository.findByMemberId(memberId)
                .stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
