package kr.somapeople.somapeopleback.domain.posts;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.boards.BoardsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
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
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    BoardsRepository boardsRepository;

    @Autowired
    UsersRepository usersRepository;

    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
        usersRepository.deleteAll();
        boardsRepository.deleteAll();
    }

    @DisplayName("게시글 저장 및 불러오기")
    @Test
    public void 게시글_저장_및_불러오기() {
        //given
        // Board 저장
        Boards board = new Boards("자유");

        boardsRepository.save(board);

        // User 저장
        Users user = Users.builder()
                .name("고병욱")
                .userType("수료생")
                .cardinalNum(13)
                .email("kohj1018@hanyang.ac.kr")
                .oauthId("djakslfjdslkacdsfadsfdsadsfac")
                .refreshToken("cdjalskjfckdlsajvkadsklcjdklasjkj")
                .agreeTerms(true)
                .build();

        usersRepository.save(user);

        // Post 저장
        String title = "test 제목";
        String content = "test 본문";
        Boolean isAnonymous = false;

        postsRepository.save(Posts.builder()
                .board(board)
                .user(user)
                .title(title)
                .content(content)
                .isAnonymous(isAnonymous)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts post = postsList.get(0);
        assertThat(post.getBoard().getBoardId()).isEqualTo(board.getBoardId());
        assertThat(post.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getIsAnonymous()).isEqualTo(isAnonymous);
        assertThat(post.getHits()).isEqualTo(0L);
        assertThat(post.getIsDelete()).isEqualTo(false);
        assertThat(post.getCreatedAt()).isBefore(LocalDateTime.now());
    }
}