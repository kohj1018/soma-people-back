package kr.somapeople.somapeopleback.web.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.somapeople.somapeopleback.service.UsersService;
import kr.somapeople.somapeopleback.web.users.dto.FirebaseTokenUpdateRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersResponseDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersSaveRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "유저 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersApiController {

    private final UsersService usersService;

    @Operation(summary = "새 유저 추가하기")
    @PostMapping()
    public Long save(@RequestBody UsersSaveRequestDto requestDto) {
        return usersService.save(requestDto);
    }

    @Operation(summary = "유저 정보 수정하기 (oauthId 로만 가능)")
    @PutMapping("/{oauthId}")
    public Long update(@PathVariable String oauthId, @RequestBody UsersUpdateRequestDto requestDto) {
        return usersService.update(oauthId, requestDto);
    }

    @Operation(summary = "userId로 유저 정보 불러오기")
    @GetMapping("/{userId}")
    public UsersResponseDto findById(@PathVariable Long userId) {
        return usersService.findById(userId);
    }

    @Operation(summary = "oauthId로 userId 불러오기")
    @GetMapping("/findUserId/{oauthId}")
    public Long findUserIdByOauthId(@PathVariable String oauthId) {
        return usersService.findUserIdByOauthId(oauthId);
    }

    @Operation(summary = "firebaseToken 등록/수정")
    @PutMapping("/firebaseToken/{userId}")
    public void registerFirebaseToken(@PathVariable Long userId, @RequestBody FirebaseTokenUpdateRequestDto requestDto) {
        usersService.registerFirebaseToken(userId, requestDto.getFirebaseToken());
    }
}
