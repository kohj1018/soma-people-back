package kr.somapeople.somapeopleback.domain.blockUserLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlockUserLogsRepository extends JpaRepository<BlockUserLogs, Long> {

    @Query(value = "SELECT l FROM BlockUserLogs l WHERE l.user.userId = ?1 AND l.blockUserId = ?2")
    Optional<BlockUserLogs> findByUserIdAndBlockUserId(Long userId, Long blockUserId);

    @Query(value = "SELECT l.blockUserId FROM BlockUserLogs l WHERE l.user.userId = ?1")
    List<Long> getAllBlockUserIdByUser(Long userId);
}
