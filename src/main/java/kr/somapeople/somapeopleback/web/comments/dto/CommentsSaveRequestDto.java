package kr.somapeople.somapeopleback.web.comments.dto;

import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentsSaveRequestDto {
    private Long postId;
    private Long userId;
    private String content;
    private Boolean isAnonymous;

    @Builder
    public CommentsSaveRequestDto(Long postId, Long userId, String content, Boolean isAnonymous) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public Comments toEntity(Posts post, Users user) {
        return Comments.builder()
                .post(post)
                .user(user)
                .content(content)
                .isAnonymous(isAnonymous)
                .build();
    }
}
