package kr.somapeople.somapeopleback.web.comments.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentsUpdateRequestDto {
    private String content;
    private Boolean isAnonymous;
    private Boolean isDelete;

    @Builder
    public CommentsUpdateRequestDto(String content, Boolean isAnonymous, Boolean isDelete) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDelete = isDelete;
    }
}
