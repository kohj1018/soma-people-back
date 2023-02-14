package kr.somapeople.somapeopleback.web.blockUserLogs.dto;

import kr.somapeople.somapeopleback.domain.blockUserLogs.BlockUserLogs;
import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlockUserLogsSaveRequestDto {
    private Long userId;
    private Long blockUserId;

    @Builder
    public BlockUserLogsSaveRequestDto(Long userId, Long blockUserId) {
        this.userId = userId;
        this.blockUserId = blockUserId;
    }

    public BlockUserLogs toEntity(Users user) {
        return BlockUserLogs.builder()
                .user(user)
                .blockUserId(blockUserId)
                .build();
    }
}
