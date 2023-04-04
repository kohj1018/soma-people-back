package kr.somapeople.somapeopleback.web.notificationLogs.dto;

import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogs;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationLogsResponseDto {
    private Long notificationLogId;
    private Long targetUserId;
    private Long postId;
    private String boardName;
    private String notificationType;
    private String content;
    private Boolean isChecked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NotificationLogsResponseDto(NotificationLogs entity) {
        this.notificationLogId = entity.getNotificationLogId();
        this.targetUserId = entity.getTargetUserId();
        this.postId = entity.getPostId();
        this.boardName = entity.getBoardName();
        this.notificationType = entity.getNotificationType();
        this.content = entity.getContent();
        this.isChecked = entity.getIsChecked();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
