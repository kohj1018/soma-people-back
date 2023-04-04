package kr.somapeople.somapeopleback.web.notificationLogs.dto;

import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogs;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationLogsSaveRequestDto {
    private Long sendingUserId;
    private Long targetUserId;
    private Long postId;
    private String boardName;
    private String notificationType;
    private String content;

    @Builder
    public NotificationLogsSaveRequestDto(Long sendingUserId, Long targetUserId, Long postId, String boardName, String notificationType, String content) {
        this.sendingUserId = sendingUserId;
        this.targetUserId = targetUserId;
        this.postId = postId;
        this.boardName = boardName;
        this.notificationType = notificationType;
        this.content = content;
    }

    public NotificationLogs toEntity() {
        return NotificationLogs.builder()
                .sendingUserId(sendingUserId)
                .targetUserId(targetUserId)
                .postId(postId)
                .boardName(boardName)
                .notificationType(notificationType)
                .content(content)
                .build();
    }
}
