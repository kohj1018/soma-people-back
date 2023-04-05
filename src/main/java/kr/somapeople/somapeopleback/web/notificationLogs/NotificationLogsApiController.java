package kr.somapeople.somapeopleback.web.notificationLogs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.somapeople.somapeopleback.service.NotificationLogsService;
import kr.somapeople.somapeopleback.web.notificationLogs.dto.AnnouncementNotificationLogSaveRequestDto;
import kr.somapeople.somapeopleback.web.notificationLogs.dto.NotificationLogsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NotificationLogs", description = "알림 기록 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notificationLogs")
public class NotificationLogsApiController {

    private final NotificationLogsService notificationLogsService;

    @Operation(summary = "모두에게 알림 기록 남기기")
    @PostMapping("/leaveNotificationLogForAll")
    public Long leaveNotificationLogForAll(@RequestBody AnnouncementNotificationLogSaveRequestDto requestDto) {
        return notificationLogsService.leaveNotificationLogForAll(requestDto);
    }

    @Operation(summary = "알림 기록 확인 완료")
    @PutMapping("/checkNotificationLog/{notificationLogId}")
    public Long checkNotification(@PathVariable Long notificationLogId) {
        return notificationLogsService.checkNotification(notificationLogId);
    }

    @Operation(summary = "알림 기록 삭제하기")
    @PutMapping("/deleteNotificationLog/{notificationLogId}")
    public Long deleteNotificationLog(@PathVariable Long notificationLogId) {
        return notificationLogsService.deleteNotificationLog(notificationLogId);
    }

    @Operation(summary = "유저가 받은 알림 기록 무한 스크롤 불러오기")
    @GetMapping()
    public List<NotificationLogsResponseDto> getNotificationLogsLowerThanId(@RequestParam Long userId, @RequestParam Long lastNotificationLogId, @RequestParam int size) {
        return notificationLogsService.fetchNotificationPagesByTargetUserId(userId, lastNotificationLogId, size);
    }
}
