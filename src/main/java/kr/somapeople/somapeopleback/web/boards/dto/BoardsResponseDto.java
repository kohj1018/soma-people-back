package kr.somapeople.somapeopleback.web.boards.dto;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardsResponseDto {
    private Long boardId;
    private String name;

    public BoardsResponseDto(Boards entity) {
        this.boardId = entity.getBoardId();
        this.name = entity.getName();
    }
}
