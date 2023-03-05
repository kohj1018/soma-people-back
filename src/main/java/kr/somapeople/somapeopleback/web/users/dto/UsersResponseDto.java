package kr.somapeople.somapeopleback.web.users.dto;

import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UsersResponseDto {
    private Long userId;
    private String name;
    private String userType;
    private Integer cardinalNum;
    private Boolean isCertified;
    private int numOfPostsWritten;
    private int numOfCommentsWritten;
    private Boolean isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UsersResponseDto(Users entity) {
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.userType = entity.getUserType();
        this.cardinalNum = entity.getCardinalNum();
        this.isCertified = entity.getIsCertified();
        this.numOfPostsWritten = (int) entity.getPostsList().stream().filter(posts -> !posts.getIsDelete()).count();
        this.numOfCommentsWritten = (int) entity.getCommentsList().stream().filter(comments -> !comments.getIsDelete()).count();
        this.isDelete = entity.getIsDelete();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
