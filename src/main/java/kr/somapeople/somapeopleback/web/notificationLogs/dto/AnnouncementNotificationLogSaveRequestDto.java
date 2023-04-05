package kr.somapeople.somapeopleback.web.notificationLogs.dto;

import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogs;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnnouncementNotificationLogSaveRequestDto {
    private Long sendingUserId;
    private String sendingUserOauthId;
    private Long postId;
    private String boardName;
    private String notificationType;
    private String content;

    @Builder
    public AnnouncementNotificationLogSaveRequestDto(Long sendingUserId, String sendingUserOauthId, Long postId, String boardName, String notificationType, String content) {
        this.sendingUserId = sendingUserId;
        this.sendingUserOauthId = sendingUserOauthId;
        this.postId = postId;
        this.boardName = boardName;
        this.notificationType = notificationType;
        this.content = content;
    }

    public NotificationLogs toEntity() {
        return NotificationLogs.builder()
            .sendingUserId(sendingUserId)
            .targetUserId(0L)
            .postId(postId)
            .boardName(boardName)
            .notificationType(notificationType)
            .content(content)
            .build();
    }
}
