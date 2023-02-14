package kr.somapeople.somapeopleback.web.blockUserLogs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.somapeople.somapeopleback.service.BlockUserLogsService;
import kr.somapeople.somapeopleback.web.blockUserLogs.dto.BlockUserLogsSaveRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "BlockUserLogs", description = "차단 유저 로그 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/blockUserLogs")
public class BlockUserLogsApiController {

    private final BlockUserLogsService blockUserLogsService;

    @Operation(summary = "유저 차단하기")
    @PostMapping()
    public Long save(@RequestBody BlockUserLogsSaveRequestDto requestDto) {
        return blockUserLogsService.save(requestDto);
    }

    @Operation(summary = "유저 차단 해제하기")
    @DeleteMapping()
    public void delete(@RequestParam Long userId, @RequestParam Long blockUserId) {
        blockUserLogsService.delete(userId, blockUserId);
    }

    @Operation(summary = "차단한 유저 모두 불러오기")
    @GetMapping("/getAllBlockUsers/{userId}")
    public List<UsersResponseDto> getAllBlockUser(@PathVariable Long userId) {
        return blockUserLogsService.getAllBlockUser(userId);
    }
}
