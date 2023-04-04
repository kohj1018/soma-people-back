package kr.somapeople.somapeopleback.domain.notificationLogs;

import kr.somapeople.somapeopleback.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "notification_logs")
@Entity
public class NotificationLogs extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_log_id")
    private Long notificationLogId;

    @JoinColumn(name = "sending_user_id", nullable = false)
    private Long sendingUserId;

    @Column(name = "target_user_id", nullable = false)
    private Long targetUserId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "board_name", length = 20, nullable = false)
    private String boardName;

    @Column(name = "notification_type", length = 20, nullable = false)
    private String notificationType;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @Builder
    public NotificationLogs(Long sendingUserId, Long targetUserId, Long postId, String boardName, String notificationType, String content) {
        this.sendingUserId = sendingUserId;
        this.targetUserId = targetUserId;
        this.postId = postId;
        this.boardName = boardName;
        this.notificationType = notificationType;
        this.content = content;
        this.isChecked = false;
        this.isDelete = false;
    }

    public void checkNotification() {
        this.isChecked = true;
    }

    public void deleteNotificationLog() {
        this.isDelete = true;
    }
}
