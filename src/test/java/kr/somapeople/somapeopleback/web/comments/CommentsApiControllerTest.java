package kr.somapeople.somapeopleback.web.comments;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.boards.BoardsRepository;
import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.comments.CommentsRepository;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.posts.PostsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsSaveRequestDto;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsUpdateRequestDto;
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
class CommentsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private UsersRepository usersRepository;

    // Board 세팅
    Boards board = new Boards("자유");

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

    // Post 세팅
    Posts post = Posts.builder()
            .board(board)
            .user(user)
            .title("test 글 제목")
            .content("test 글 본문")
            .isAnonymous(false)
            .build();

    @BeforeEach
    public void setup() throws Exception {
        boardsRepository.save(board);
        usersRepository.save(user);
        postsRepository.save(post);
    }

    @AfterEach
    public void tearDown() throws Exception {
        commentsRepository.deleteAll();
        postsRepository.deleteAll();
        usersRepository.deleteAll();
        boardsRepository.deleteAll();
    }

    @DisplayName("Comment가 등록된다")
    @Test
    public void Comment_등록된다() throws Exception {
        //given
        String content = "test 댓글 본문";
        Boolean isAnonymous = false;

        CommentsSaveRequestDto requestDto = CommentsSaveRequestDto.builder()
                .postId(post.getPostId())
                .userId(user.getUserId())
                .content(content)
                .isAnonymous(isAnonymous)
                .build();

        String url = "http://localhost:" + port + "/api/v1/comments";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Comments> all = commentsRepository.findAll();
        assertThat(all.get(0).getPost().getPostId()).isEqualTo(post.getPostId());
        assertThat(all.get(0).getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getIsAnonymous()).isEqualTo(isAnonymous);
        assertThat(all.get(0).getIsDelete()).isEqualTo(false);
        assertThat(all.get(0).getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @DisplayName("Comment가 수정된다")
    @Test
    public void Comment_수정된다() throws Exception {
        //given
        // 초기 저장 값
        Long updateId = commentsRepository.save(Comments.builder()
                .post(post)
                .user(user)
                .content("test 댓글 본문")
                .isAnonymous(false)
                .build()).getCommentId();

        // 이후 수정 값
        String expectedContent = "수정된 test 댓글 본문";
        Boolean expectedIsDelete = true;

        CommentsUpdateRequestDto requestDto = CommentsUpdateRequestDto.builder()
                .content(expectedContent)
                .isAnonymous(false)
                .isDelete(expectedIsDelete)
                .build();

        String url = "http://localhost:" + port + "/api/v1/comments/" + updateId;

        HttpEntity<CommentsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Comments> all = commentsRepository.findAll();
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
        assertThat(all.get(0).getIsDelete()).isEqualTo(expectedIsDelete);
        assertThat(all.get(0).getUpdatedAt()).isBefore(LocalDateTime.now());
    }
}