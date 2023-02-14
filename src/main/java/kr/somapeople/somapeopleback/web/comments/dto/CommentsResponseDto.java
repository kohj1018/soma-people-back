package kr.somapeople.somapeopleback.web.comments.dto;

import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.web.users.dto.UsersResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentsResponseDto {
    private Long commentId;
    private Long postId;
    private UsersResponseDto user;
    private String content;
    private Boolean isAnonymous;
    private Boolean isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentsResponseDto(Comments entity) {
        this.commentId = entity.getCommentId();
        this.postId = entity.getPost().getPostId();
        this.user = new UsersResponseDto(entity.getUser());
        this.content = entity.getContent();
        this.isAnonymous = entity.getIsAnonymous();
        this.isDelete = entity.getIsDelete();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
