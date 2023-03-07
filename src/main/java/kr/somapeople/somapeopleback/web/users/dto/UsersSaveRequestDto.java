package kr.somapeople.somapeopleback.web.users.dto;

import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersSaveRequestDto {
    private String name;
    private String userType;
    private Integer cardinalNum;
    private String email;
    private String oauthId;
    private String refreshToken;
    private Boolean agreeTerms;

    @Builder
    public UsersSaveRequestDto(String name, String userType, Integer cardinalNum, String email, String oauthId, String refreshToken, Boolean agreeTerms) {
        this.name = name;
        this.userType = userType;
        this.cardinalNum = cardinalNum;
        this.email = email;
        this.oauthId = oauthId;
        this.refreshToken = refreshToken;
        this.agreeTerms = agreeTerms;
    }

    public Users toEntity() {
        return Users.builder()
                .name(name)
                .userType(userType)
                .cardinalNum(cardinalNum)
                .email(email)
                .oauthId(oauthId)
                .refreshToken(refreshToken)
                .agreeTerms(agreeTerms)
                .build();
    }
}
