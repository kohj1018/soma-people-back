package kr.somapeople.somapeopleback.domain.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UsersRepositoryTest {

    @Autowired
    UsersRepository usersRepository;

    @AfterEach
    public void cleanup() {
        usersRepository.deleteAll();
    }

    @DisplayName("유저저장 불러오기")
    @Test
    public void 유저저장_불러오기() {
        //given
        String name = "고병욱";
        String userType = "수료생";
        Integer cardinalNum = 13;
        Boolean isCertified = true;
        String oauthId = "djakslfjdslkacdsfadsfdsadsfac";
        String refreshToken = "cdjalskjfckdlsajvkadsklcjdklasjkj";
        Boolean agreeTerms = true;
        Boolean isDelete = false;

        usersRepository.save(Users.builder()
                .name(name)
                .userType(userType)
                .cardinalNum(cardinalNum)
                .isCertified(isCertified)
                .oauthId(oauthId)
                .refreshToken(refreshToken)
                .agreeTerms(agreeTerms)
                .isDelete(isDelete)
                .build());

        //when
        List<Users> usersList = usersRepository.findAll();

        //then
        Users user = usersList.get(0);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getUserType()).isEqualTo(userType);
        assertThat(user.getCardinalNum()).isEqualTo(cardinalNum);
        assertThat(user.getIsCertified()).isEqualTo(isCertified);
        assertThat(user.getOauthId()).isEqualTo(oauthId);
        assertThat(user.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(user.getAgreeTerms()).isEqualTo(agreeTerms);
        assertThat(user.getIsDelete()).isEqualTo(isDelete);
        assertThat(user.getCreatedAt()).isBefore(LocalDateTime.now());
    }
}