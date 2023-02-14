package kr.somapeople.somapeopleback.web.posts.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private Long boardId;
    private String title;
    private String content;
    private Boolean isAnonymous;
    private Boolean isDelete;

    @Builder
    public PostsUpdateRequestDto(Long boardId, String title, String content, Boolean isAnonymous, Boolean isDelete) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDelete = isDelete;
    }
}
