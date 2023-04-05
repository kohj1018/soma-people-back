package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogs;
import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.notificationLogs.dto.AnnouncementNotificationLogSaveRequestDto;
import kr.somapeople.somapeopleback.web.notificationLogs.dto.NotificationLogsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationLogsService {

    private final NotificationLogsRepository notificationLogsRepository;
    private final UsersRepository usersRepository;

//    @Transactional
//    public Long save(NotificationLogsSaveRequestDto requestDto) {
//        if (Objects.equals(requestDto.getTargetUserId(), requestDto.getSendingUserId())) {
//            throw new IllegalArgumentException("자기 자신에게 알림을 보낼 수 없습니다.");
//        }
//
//        return notificationLogsRepository.save(requestDto.toEntity()).getNotificationLogId();
//    }

    @Transactional
    public Long leaveNotificationLogForAll(AnnouncementNotificationLogSaveRequestDto requestDto) {
        Users user = usersRepository.findByOauthId(requestDto.getSendingUserOauthId())
            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. oauthId=" + requestDto.getSendingUserOauthId()));

        if (Objects.equals(user.getUserType(), "관리자") || Objects.equals(user.getUserType(), "사무국")) {
            return notificationLogsRepository.save(requestDto.toEntity()).getNotificationLogId();
        } else {
            throw new IllegalArgumentException("권한이 없습니다. userType=" + user.getUserType());
        }
    }

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

    public List<NotificationLogsResponseDto> fetchNotificationPagesByTargetUserId(Long targetUserId, Long lastNotificationLogId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<NotificationLogs> entityPage = notificationLogsRepository.findByTargetUserIdLessThanOrderByNotificationLogIdDesc(targetUserId, lastNotificationLogId, pageRequest);
        List<NotificationLogs> entityList = entityPage.getContent();

        return entityList.stream()
                .map(NotificationLogsResponseDto::new)
                .collect(Collectors.toList());
    }
}
