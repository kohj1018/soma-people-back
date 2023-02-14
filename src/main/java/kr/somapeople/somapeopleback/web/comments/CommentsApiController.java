package kr.somapeople.somapeopleback.web.comments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.somapeople.somapeopleback.service.CommentsService;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsResponseDto;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsSaveRequestDto;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments", description = "댓글 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class CommentsApiController {

    private final CommentsService commentsService;

    @Operation(summary = "새 댓글 추가하기")
    @PostMapping()
    public Long save(@RequestBody CommentsSaveRequestDto requestDto) {
        return commentsService.save(requestDto);
    }

    @Operation(summary = "댓글 수정하기")
    @PutMapping("/{commentId}")
    public Long update(@PathVariable Long commentId, @RequestBody CommentsUpdateRequestDto requestDto) {
        return commentsService.update(commentId, requestDto);
    }

    @Operation(summary = "게시글에 달린 댓글 모두 불러오기")
    @GetMapping()
    public List<CommentsResponseDto> findAllCommentsOnPost(@RequestParam Long postId, @RequestParam Long userId) {
        return commentsService.findAllCommentsOnPost(postId, userId);
    }

    @Operation(summary = "유저가 작성한 댓글 모두 불러오기")
    @GetMapping("/writtenByUser/{userId}")
    public List<CommentsResponseDto> findByUserId(@PathVariable Long userId) {
        return commentsService.findByUserId(userId);
    }
}
