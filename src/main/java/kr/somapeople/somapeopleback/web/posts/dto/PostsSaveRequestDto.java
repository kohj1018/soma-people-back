package kr.somapeople.somapeopleback.web.posts.dto;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private Long boardId;
    private Long userId;
    private String title;
    private String content;
    private Boolean isAnonymous;

    @Builder
    public PostsSaveRequestDto(Long boardId, Long userId, String title, String content, Boolean isAnonymous) {
        this.boardId = boardId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public Posts toEntity(Boards board, Users user) {
        return Posts.builder()
                .board(board)
                .user(user)
                .title(title)
                .content(content)
                .isAnonymous(isAnonymous)
                .build();
    }
}
