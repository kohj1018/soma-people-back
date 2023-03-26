package kr.somapeople.somapeopleback.web.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirebaseTokenUpdateRequestDto {
    private String firebaseToken;

    @Builder
    public FirebaseTokenUpdateRequestDto(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
