package kr.somapeople.somapeopleback.domain.comments;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.boards.BoardsRepository;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.posts.PostsRepository;
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
class CommentsRepositoryTest {

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BoardsRepository boardsRepository;

    @AfterEach
    public void cleanup() {
        commentsRepository.deleteAll();
        postsRepository.deleteAll();
        usersRepository.deleteAll();
        boardsRepository.deleteAll();
    }

    @DisplayName("답변글 저장 및 불러오기")
    @Test
    public void 답변글_저장_및_불러오기() {
        //given
        // Board 세팅
        Boards board = new Boards("자유");

        boardsRepository.save(board);

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

        usersRepository.save(user);

        // Post 세팅
        Posts post = Posts.builder()
                .board(board)
                .user(user)
                .title("test 제목")
                .content("test 본문")
                .isAnonymous(false)
                .build();

        postsRepository.save(post);

        // Comment 저장
        String content = "test 댓글 본문";
        Boolean isAnonymous = false;

        commentsRepository.save(Comments.builder()
                .post(post)
                .user(user)
                .content(content)
                .isAnonymous(isAnonymous)
                .build());

        //when
        List<Comments> commentsList = commentsRepository.findAll();

        //then
        Comments comment = commentsList.get(0);
        assertThat(comment.getPost().getPostId()).isEqualTo(post.getPostId());
        assertThat(comment.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getIsAnonymous()).isEqualTo(isAnonymous);
        assertThat(comment.getIsDelete()).isEqualTo(false);
        assertThat(comment.getCreatedAt()).isBefore(LocalDateTime.now());
    }
}