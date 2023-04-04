package kr.somapeople.somapeopleback.domain.notificationLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationLogsRepository extends JpaRepository<NotificationLogs, Long> {

    @Query(value = "SELECT nl FROM NotificationLogs nl WHERE nl.targetUserId = ?1 AND nl.isDelete = false ORDER BY nl.notificationLogId DESC")
    List<NotificationLogs> findByTargetUserId(Long targetUserId);
}
