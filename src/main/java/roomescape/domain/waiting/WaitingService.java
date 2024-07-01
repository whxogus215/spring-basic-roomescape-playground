package roomescape.domain.waiting;

import org.springframework.stereotype.Service;
import roomescape.domain.member.dto.LoginMember;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.domain.waiting.dto.WaitingRequest;
import roomescape.domain.waiting.dto.WaitingResponse;
import roomescape.domain.waiting.entity.Waiting;
import roomescape.domain.waiting.repository.WaitingRepository;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    /**
     * 서비스가 의존하는 리포지토리가 이보다 더 많아진다면 어떻게 해야될까?
     * - 서비스를 다시 쪼갠다.
     * - Facade 패턴 도입?
     */
    public WaitingService(final WaitingRepository waitingRepository,
                          final MemberRepository memberRepository,
                          final TimeRepository timeRepository,
                          final ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse save(final WaitingRequest waitingRequest, final LoginMember loginMember) {
        /**
         * 예약 대기를 저장하기 위해 필요한 것
         * 1. Member
         * 2. Time
         * 3. Theme
         */
        final Member findMember = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        final Time findTime = timeRepository.findById(waitingRequest.time())
                .orElseThrow(() -> new RuntimeException("예약 시간이 존재하지 않습니다."));
        final Theme findTheme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(() -> new RuntimeException("예약 테마가 존재하지 않습니다."));
        final Waiting savedWaiting = waitingRepository.save(new Waiting(
                waitingRequest.date(),
                findMember,
                findTime,
                findTheme
        ));
        return WaitingResponse.from(savedWaiting);
    }

    public void deleteWaiting(Long id) {
        waitingRepository.deleteById(id);
    }
}
