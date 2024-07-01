package roomescape.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailAndPassword(String email, String password);
}
