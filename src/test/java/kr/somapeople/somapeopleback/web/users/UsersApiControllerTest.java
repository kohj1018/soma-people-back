package kr.somapeople.somapeopleback.web.users;


import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.users.dto.UsersSaveRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsersRepository usersRepository;

    @AfterEach
    public void tearDown() throws Exception {
        usersRepository.deleteAll();
    }

    @DisplayName("users가 추가된다")
    @Test
    public void users_추가된다() throws Exception {
        //given
        String name = "고병욱";
        String userType = "수료생";
        Integer cardinalNum = 13;
        String email = "kohj1018@hanyang.ac.kr";
        String oauthId = "djakslfjdslkacdsfadsfdsadsfac";
        String refreshToken = "cdjalskjfckdlsajvkadsklcjdklasjkj";
        Boolean agreeTerms = true;
        UsersSaveRequestDto requestDto = UsersSaveRequestDto.builder()
                .name(name)
                .userType(userType)
                .cardinalNum(cardinalNum)
                .email(email)
                .oauthId(oauthId)
                .refreshToken(refreshToken)
                .agreeTerms(agreeTerms)
                .build();

        String url = "http://localhost:" + port + "/api/v1/users";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Users> all = usersRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
        assertThat(all.get(0).getUserType()).isEqualTo(userType);
        assertThat(all.get(0).getCardinalNum()).isEqualTo(cardinalNum);
        assertThat(all.get(0).getEmail()).isEqualTo(email);
        assertThat(all.get(0).getOauthId()).isEqualTo(oauthId);
        assertThat(all.get(0).getRefreshToken()).isEqualTo(refreshToken);
        assertThat(all.get(0).getAgreeTerms()).isEqualTo(agreeTerms);
    }

    @DisplayName("users가 수정된다")
    @Test
    public void users_수정된다() throws Exception {
        //given
        Users savedUser = usersRepository.save(Users.builder()
                .name("고병욱")
                .userType("수료생")
                .cardinalNum(13)
                .email("kohj1018@hanyang.ac.kr")
                .oauthId("djakslfjdslkacdsfadsfdsadsfac")
                .refreshToken("cdjalskjfckdlsajvkadsklcjdklasjkj")
                .agreeTerms(true)
                .build());

        String updateOauthId = savedUser.getOauthId();
        String expectedName = "고병욱";
        Boolean expectedIsDelete = true;

        UsersUpdateRequestDto requestDto = UsersUpdateRequestDto.builder()
                .name(expectedName)
                .isDelete(expectedIsDelete)
                .build();

        String url = "http://localhost:" + port + "/api/v1/users/" + updateOauthId;

        HttpEntity<UsersUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Users> all = usersRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(expectedName);
        assertThat(all.get(0).getIsDelete()).isEqualTo(expectedIsDelete);
    }
}