package kr.somapeople.somapeopleback.web.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCertificationUpdateRequestDto {
    private String adminOauthId;
    private Long targetUserId;

    @Builder
    public UserCertificationUpdateRequestDto(String adminOauthId, Long targetUserId) {
        this.adminOauthId = adminOauthId;
        this.targetUserId = targetUserId;
    }
}
