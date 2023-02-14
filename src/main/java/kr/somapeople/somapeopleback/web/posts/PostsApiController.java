package kr.somapeople.somapeopleback.web.posts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.somapeople.somapeopleback.service.PostsService;
import kr.somapeople.somapeopleback.web.posts.dto.PostsResponseDto;
import kr.somapeople.somapeopleback.web.posts.dto.PostsSaveRequestDto;
import kr.somapeople.somapeopleback.web.posts.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Posts", description = "게시글 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostsApiController {

    private final PostsService postsService;

    @Operation(summary = "새 게시글 등록하기")
    @PostMapping()
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @Operation(summary = "게시글 수정하기")
    @PutMapping("/{postId}")
    public Long update(@PathVariable Long postId, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(postId, requestDto);
    }

    @Operation(summary = "게시글 ID로 게시글 정보 불러오기")
    @GetMapping("/{postId}")
    public PostsResponseDto findById(@PathVariable Long postId) {
        return postsService.findById(postId);
    }

    @Operation(summary = "게시글 무한 스크롤 불러오기")
    @GetMapping()
    public List<PostsResponseDto> getPostsLowerThanId(@RequestParam Long boardId, @RequestParam Long lastPostId, @RequestParam int size) {
        return postsService.fetchPostPagesBy(boardId, lastPostId, size);
    }

    @Operation(summary = "게시글 검색하기")
    @GetMapping("/search")
    public List<PostsResponseDto> getSearchedPosts(@RequestParam String searchTerm, @RequestParam Long boardIdToSearch) {
        return postsService.searchPosts(searchTerm, boardIdToSearch);
    }

    @Operation(summary = "유저가 작성한 게시글 모두 불러오기")
    @GetMapping("/writtenByUser/{userId}")
    public List<PostsResponseDto> findByUserId(@PathVariable Long userId) {
        return postsService.findByUserId(userId);
    }

    @Operation(summary = "조회수 1만큼 증가")
    @PutMapping("/hits/{postId}")
    public void addHits(@PathVariable Long postId, @RequestParam Long userId) {
        postsService.addHits(postId, userId);
    }
}
