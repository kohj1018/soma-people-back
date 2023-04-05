package kr.somapeople.somapeopleback.domain.notificationLogs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationLogsRepository extends JpaRepository<NotificationLogs, Long> {

    // targetUserId가 0인 경우는 전체에게 알림 기록을 남긴 경우
    @Query(value = "SELECT nl FROM NotificationLogs nl WHERE (nl.targetUserId = ?1 OR nl.targetUserId = 0) AND nl.notificationLogId < ?2 AND nl.isDelete = false ORDER BY nl.notificationLogId DESC")
    Page<NotificationLogs> findByTargetUserIdLessThanOrderByNotificationLogIdDesc(Long targetUserId, Long lastNotificationLogId, PageRequest pageRequest);
}
