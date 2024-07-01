package roomescape.domain.waiting.dto;

import roomescape.domain.waiting.entity.Waiting;

public record WaitingResponse(
        String theme,
        String date,
        String time
) {

    public static WaitingResponse from(final Waiting waiting) {
        return new WaitingResponse(
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getTime()
        );
    }
}
