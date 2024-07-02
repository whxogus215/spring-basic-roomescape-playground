package roomescape.domain.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.dto.LoginMember;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.domain.reservation.dto.MyReservationResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.domain.waiting.dto.WaitingWithRank;
import roomescape.domain.waiting.repository.WaitingRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final TimeRepository timeRepository,
                              final ThemeRepository themeRepository,
                              final MemberRepository memberRepository,
                              final WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
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
        final Member findMember = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        validateDuplicatedReservation(reservationRequest, findTheme, findTime);

        Reservation reservation = reservationRepository.save(
                reservationRequest.toEntity(findTime, findTheme, findMember));

        return new ReservationResponse(
                reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(),
                reservation.getDate(), reservation.getTime().getTime());
    }

    private void validateDuplicatedReservation(final ReservationRequest request,
                                               final Theme theme,
                                               final Time time) {
        reservationRepository.findByDateAndThemeIdAndTimeId(request.getDate(), theme.getId(), time.getId())
                .ifPresent(reservation -> {
                    throw new RuntimeException("이미 예약되었습니다.");
                });
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(Long memberId) {
        final List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        final List<WaitingWithRank> waitingWithRanks =
                waitingRepository.findWaitingWithRankByMemberId(memberId);
        return MyReservationResponse.of(reservations, waitingWithRanks);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
