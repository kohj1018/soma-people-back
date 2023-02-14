package kr.somapeople.somapeopleback.web.users.dto;

import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersResponseDto {
    private Long userId;
    private String name;
    private String userType;
    private Integer cardinalNum;
    private Boolean isCertified;
    private Boolean isDelete;

    public UsersResponseDto(Users entity) {
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.userType = entity.getUserType();
        this.cardinalNum = entity.getCardinalNum();
        this.isCertified = entity.getIsCertified();
        this.isDelete = entity.getIsDelete();
    }
}
