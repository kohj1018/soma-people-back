package kr.somapeople.somapeopleback.web.comments.dto;

import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class CommentsSaveRequestDto {
    private Long postId;
    private Long userId;
    private Long refId;
    private String content;
    private Boolean isAnonymous;

    @Builder
    public CommentsSaveRequestDto(Long postId, Long userId, Long refId, String content, Boolean isAnonymous) {
        this.postId = postId;
        this.userId = userId;
        this.refId = Objects.requireNonNullElse(refId, 0L);
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public Comments toEntity(Posts post, Users user) {
        return Comments.builder()
                .post(post)
                .user(user)
                .refId(refId)
                .content(content)
                .isAnonymous(isAnonymous)
                .build();
    }
}
