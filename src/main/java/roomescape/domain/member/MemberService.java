package roomescape.domain.member;

import org.springframework.stereotype.Service;
import roomescape.domain.auth.JwtTokenManager;
import roomescape.domain.member.dto.MemberLoginRequest;
import roomescape.domain.member.dto.MemberRequest;
import roomescape.domain.member.dto.MemberResponse;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenManager jwtTokenManager;

    public MemberService(final MemberRepository memberRepository,
                         final JwtTokenManager jwtTokenManager) {
        this.memberRepository = memberRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(
                        memberRequest.getName(), memberRequest.getEmail(),
                        memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String findMember(MemberLoginRequest memberLoginRequest) {
        final Member member = memberRepository.findByEmailAndPassword(
                memberLoginRequest.email(), memberLoginRequest.password());
        return jwtTokenManager.createToken(member);
    }
}
