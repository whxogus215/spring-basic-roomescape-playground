package roomescape.domain.member;

import org.springframework.stereotype.Service;
import roomescape.domain.auth.JwtTokenManager;
import roomescape.domain.member.dto.MemberLoginRequest;
import roomescape.domain.member.dto.MemberRequest;
import roomescape.domain.member.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtTokenManager jwtTokenManager;

    public MemberService(MemberDao memberDao, JwtTokenManager jwtTokenManager) {
        this.memberDao = memberDao;
        this.jwtTokenManager = jwtTokenManager;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(
                        memberRequest.getName(), memberRequest.getEmail(),
                        memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String findMember(MemberLoginRequest memberLoginRequest) {
        final Member member = memberDao.findByEmailAndPassword(
                memberLoginRequest.email(), memberLoginRequest.password());
        return jwtTokenManager.createToken(member);
    }
}
