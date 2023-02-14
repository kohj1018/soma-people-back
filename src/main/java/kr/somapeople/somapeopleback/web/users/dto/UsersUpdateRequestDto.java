package kr.somapeople.somapeopleback.web.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersUpdateRequestDto {
    private String name;
    private Boolean isDelete;

    @Builder
    public UsersUpdateRequestDto(String name, Boolean isDelete) {
        this.name = name;
        this.isDelete = isDelete;
    }
}
