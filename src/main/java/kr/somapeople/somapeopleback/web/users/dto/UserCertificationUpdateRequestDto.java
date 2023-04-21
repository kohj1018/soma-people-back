package kr.somapeople.somapeopleback.web.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCertificationUpdateRequestDto {
    private String adminOauthId;
    private Long targetUserId;
    private String nameToBeUpdated;
    private Integer cardinalNumToBeUpdated;

    @Builder
    public UserCertificationUpdateRequestDto(String adminOauthId, Long targetUserId, String nameToBeUpdated, Integer cardinalNumToBeUpdated) {
        this.adminOauthId = adminOauthId;
        this.targetUserId = targetUserId;
        this.nameToBeUpdated = nameToBeUpdated;
        this.cardinalNumToBeUpdated = cardinalNumToBeUpdated;
    }
}
