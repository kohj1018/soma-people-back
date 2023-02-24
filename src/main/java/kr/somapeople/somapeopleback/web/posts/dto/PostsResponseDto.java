package kr.somapeople.somapeopleback.web.posts.dto;

import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.web.boards.dto.BoardsResponseDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsResponseDto {
    private Long postId;
    private BoardsResponseDto board;
    private UsersResponseDto user;
    private String title;
    private String content;
    private Boolean isAnonymous;
    private Long hits;
    private int commentsNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostsResponseDto(Posts entity) {
        this.postId = entity.getPostId();
        this.board = new BoardsResponseDto(entity.getBoard());
        this.user = new UsersResponseDto(entity.getUser());
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.isAnonymous = entity.getIsAnonymous();
        this.hits = entity.getHits();
        this.commentsNum = entity.getCommentsList().size();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
