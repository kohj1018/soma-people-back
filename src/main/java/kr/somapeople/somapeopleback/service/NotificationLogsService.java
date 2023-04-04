package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogs;
import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogsRepository;
import kr.somapeople.somapeopleback.web.notificationLogs.dto.NotificationLogsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationLogsService {

    private final NotificationLogsRepository notificationLogsRepository;

//    @Transactional
//    public Long save(NotificationLogsSaveRequestDto requestDto) {
//        if (Objects.equals(requestDto.getTargetUserId(), requestDto.getSendingUserId())) {
//            throw new IllegalArgumentException("자기 자신에게 알림을 보낼 수 없습니다.");
//        }
//
//        return notificationLogsRepository.save(requestDto.toEntity()).getNotificationLogId();
//    }

    @Transactional
    public Long checkNotification(Long notificationLogId) {
        NotificationLogs notificationLog = notificationLogsRepository.findById(notificationLogId)
            .orElseThrow(() -> new IllegalArgumentException("해당 알림 기록이 존재하지 않습니다. id=" + notificationLogId));

        notificationLog.checkNotification();

        return notificationLogId;
    }

    @Transactional
    public Long deleteNotificationLog(Long notificationLogId) {
        NotificationLogs notificationLog = notificationLogsRepository.findById(notificationLogId)
            .orElseThrow(() -> new IllegalArgumentException("해당 알림 기록이 존재하지 않습니다. id=" + notificationLogId));

        notificationLog.deleteNotificationLog();

        return notificationLogId;
    }

    public List<NotificationLogsResponseDto> findByTargetUserId(Long targetUserId) {
        List<NotificationLogs> notificationLogsList = notificationLogsRepository.findByTargetUserId(targetUserId);

        return notificationLogsList.stream()
                .map(NotificationLogsResponseDto::new)
                .collect(Collectors.toList());
    }
}
