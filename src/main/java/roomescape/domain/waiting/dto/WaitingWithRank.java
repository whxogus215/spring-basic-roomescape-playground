package roomescape.domain.waiting.dto;

import roomescape.domain.waiting.entity.Waiting;

public record WaitingWithRank(
        Waiting waiting,
        Long rank
) {
}
