package kr.somapeople.somapeopleback.web.posts;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.boards.BoardsRepository;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.posts.PostsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.posts.dto.PostsSaveRequestDto;
import kr.somapeople.somapeopleback.web.posts.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private UsersRepository usersRepository;

    // Board 세팅
    Boards board = new Boards("자유");
    Boards board2 = new Boards("질문답변");

    // User 세팅
    Users user = Users.builder()
            .name("고병욱")
            .userType("수료생")
            .cardinalNum(13)
            .isCertified(true)
            .oauthId("djakslfjdslkacdsfadsfdsadsfac")
            .refreshToken("cdjalskjfckdlsajvkadsklcjdklasjkj")
            .agreeTerms(true)
            .build();

    @BeforeEach
    public void setup() throws Exception {
        boardsRepository.save(board);
        boardsRepository.save(board2);
        usersRepository.save(user);
    }

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
        usersRepository.deleteAll();
        boardsRepository.deleteAll();
    }

    @DisplayName("Post가 등록된다")
    @Test
    public void Posts_등록된다() throws Exception {
        //given
        String title = "test 제목";
        String content = "test 본문";
        Boolean isAnonymous = false;

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .boardId(board.getBoardId())
                .userId(user.getUserId())
                .title(title)
                .content(content)
                .isAnonymous(isAnonymous)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getBoard().getBoardId()).isEqualTo(board.getBoardId());
        assertThat(all.get(0).getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getIsAnonymous()).isEqualTo(isAnonymous);
        assertThat(all.get(0).getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @DisplayName("Post가 수정된다")
    @Test
    public void Posts_수정된다() throws Exception {
        //given
        // 초기 저장 값
        Long updateId = postsRepository.save(Posts.builder()
                .board(board)
                .user(user)
                .title("test 제목")
                .content("test 본문")
                .isAnonymous(false)
                .build()).getPostId();

        // 이후 수정 값
        String expectedTitle = "수정된 test 제목";
        String expectedContent = "수정된 test 본문";
        Boolean expectedIsDelete = true;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .boardId(board2.getBoardId())
                .title(expectedTitle)
                .content(expectedContent)
                .isAnonymous(false)
                .isDelete(expectedIsDelete)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getBoard().getBoardId()).isEqualTo(board2.getBoardId());
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
        assertThat(all.get(0).getIsDelete()).isEqualTo(expectedIsDelete);
        assertThat(all.get(0).getUpdatedAt()).isNotNull();
    }
}