package roomescape.domain.waiting.dto;

import roomescape.domain.waiting.entity.Waiting;

public record WaitingResponse(
        String theme,
        String date,
        String time,
        Long waitingNumber
) {

    public static WaitingResponse of(final Waiting waiting,
                                     final Long waitingNumber) {
        return new WaitingResponse(
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getTime(),
                waitingNumber
        );
    }
}
