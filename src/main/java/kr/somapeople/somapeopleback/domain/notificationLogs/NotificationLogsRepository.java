package kr.somapeople.somapeopleback.domain.notificationLogs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationLogsRepository extends JpaRepository<NotificationLogs, Long> {

    @Query(value = "SELECT nl FROM NotificationLogs nl WHERE nl.targetUserId = ?1 AND nl.notificationLogId < ?2 AND nl.isDelete = false ORDER BY nl.notificationLogId DESC")
    Page<NotificationLogs> findByTargetUserIdLessThanOrderByNotificationLogIdDesc(Long targetUserId, Long lastNotificationLogId, PageRequest pageRequest);
}
