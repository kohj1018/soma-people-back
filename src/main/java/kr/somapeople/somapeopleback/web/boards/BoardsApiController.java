package kr.somapeople.somapeopleback.web.boards;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.somapeople.somapeopleback.service.BoardsService;
import kr.somapeople.somapeopleback.web.boards.dto.BoardsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Boards", description = "게시판 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
public class BoardsApiController {

    private final BoardsService boardsService;

    @Operation(summary = "전체 게시판 불러오기")
    @GetMapping()
    public List<BoardsResponseDto> findAll() {
        return boardsService.findAll();
    }
}
