package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.blockUserLogs.BlockUserLogsRepository;
import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.boards.BoardsRepository;
import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.comments.CommentsRepository;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.posts.PostsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.posts.dto.MainPagePostsResponseDto;
import kr.somapeople.somapeopleback.web.posts.dto.PostsResponseDto;
import kr.somapeople.somapeopleback.web.posts.dto.PostsSaveRequestDto;
import kr.somapeople.somapeopleback.web.posts.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final BoardsRepository boardsRepository;
    private final UsersRepository usersRepository;
    private final BlockUserLogsRepository blockUserLogsRepository;
    private final CommentsRepository commentsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Boards board = boardsRepository.findById(requestDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다. id=" + requestDto.getBoardId()));

        Users user = usersRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + requestDto.getUserId()));

        return postsRepository.save(requestDto.toEntity(board, user)).getPostId();
    }

    @Transactional
    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        Boards board = boardsRepository.findById(requestDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다. id=" + requestDto.getBoardId()));

        if (requestDto.getIsDelete()) { // 게시글 삭제 시 댓글도 모두 삭제
            List<Comments> commentsList = commentsRepository.findAllCommentsOnPostForDelete(postId);
            commentsList.forEach(comments -> {
                comments.update(comments.getContent(), comments.getIsAnonymous(), true);
            });
        }

        post.update(board, requestDto.getTitle(), requestDto.getContent(), requestDto.getIsAnonymous(), requestDto.getIsDelete());

        return post.getPostId();
    }

    @Transactional
    public void addHits(Long postId, Long userId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + userId));

        post.addHits(post.getUser().getUserId(), user.getUserId());
    }

    public PostsResponseDto findById(Long postId) {
        Posts entity = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        return new PostsResponseDto(entity);
    }

    public List<PostsResponseDto> fetchPostPagesBy(Long boardId, Long lastPostId, int size, Long userId) {
        List<Long> blockUserIdList = blockUserLogsRepository.getAllBlockUserIdByUser(userId);   // user가 차단한 유저들 id 목록을 불러옴

        if (blockUserIdList.isEmpty()) {    // TODO : SQL의 NOT IN에 빈 리스트가 들어가면 값 반환이 안돼 0을 추가해줌. (임시 해결책이라 추후 NOT IN 대신 LEFT JOIN을 활용한 방식으로 해결할 수 있을 듯)
            blockUserIdList.add(0L);
        }

        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Posts> entityPage = postsRepository.findByPostIdLessThanOrderByPostIdDesc(boardId, lastPostId, blockUserIdList, pageRequest);
        List<Posts> entityList = entityPage.getContent();

        return entityList.stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PostsResponseDto> searchPosts(String searchTerm, Long boardIdToSearch, Long userId) {
        List<Long> blockUserIdList = blockUserLogsRepository.getAllBlockUserIdByUser(userId);   // user가 차단한 유저들 id 목록을 불러옴

        if (blockUserIdList.isEmpty()) {    // TODO : SQL의 NOT IN에 빈 리스트가 들어가면 값 반환이 안돼 0을 추가해줌. (임시 해결책이라 추후 NOT IN 대신 LEFT JOIN을 활용한 방식으로 해결할 수 있을 듯)
            blockUserIdList.add(0L);
        }

        // 공백, 쉼표, 하이픈을 기준으로 split
        String[] searchTermList = searchTerm.split("\\s|,|-");

        List<PostsResponseDto> responseDtoList = new ArrayList<>();
        List<Long> containIdList = new ArrayList<>();

        for (String term : searchTermList) {
            List<Posts> entityList = new ArrayList<>();
            if (boardIdToSearch < 1) {  // boardId가 1보다 작은 경우 전체 게시판 검색을 의미
                entityList = postsRepository.searchAllPosts(term, blockUserIdList);
            } else {
                entityList = postsRepository.searchPostsWithinGivenBoard(term, boardIdToSearch, blockUserIdList);
            }

            if (!entityList.isEmpty()) {
                entityList.forEach(entity -> {
                    if (!containIdList.contains(entity.getPostId())) {  // 중복된 검색 결과는 제외
                        responseDtoList.add(new PostsResponseDto(entity));
                        containIdList.add(entity.getPostId());
                    }
                });
            }
        }

        if (!responseDtoList.isEmpty()) {   // 최근에 작성된 글 순으로 정렬
            responseDtoList.sort(new Comparator<PostsResponseDto>() {
                @Override
                public int compare(PostsResponseDto o1, PostsResponseDto o2) {
                    return o2.getPostId().intValue() - o1.getPostId().intValue();
                }
            });
        }

        return responseDtoList;
    }

    public List<PostsResponseDto> findByUserId(Long userId) {
        List<Posts> entityList = postsRepository.findByUserId(userId);

        return entityList.stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }

    public MainPagePostsResponseDto getPostFromEachBoard(Long userId) {
        List<Long> blockUserIdList = blockUserLogsRepository.getAllBlockUserIdByUser(userId);   // user가 차단한 유저들 id 목록을 불러옴

        if (blockUserIdList.isEmpty()) {    // TODO : SQL의 NOT IN에 빈 리스트가 들어가면 값 반환이 안돼 0을 추가해줌. (임시 해결책이라 추후 NOT IN 대신 LEFT JOIN을 활용한 방식으로 해결할 수 있을 듯)
            blockUserIdList.add(0L);
        }

        List<Long> boardIdList = new ArrayList<>();
        boardIdList.add(1L);
        boardIdList.add(2L);
        boardIdList.add(3L);
        boardIdList.add(4L);
        boardIdList.add(1000L);

        Optional<Users> user = usersRepository.findById(userId);
        user.ifPresent(userInfo -> {
            if (userInfo.getIsCertified()) {
                if (Objects.equals(userInfo.getUserType(), "연수생")) {    // 본인 기수 게시판은 볼 수 있도록 추가
                    boardIdList.add(Long.parseLong("1" + userInfo.getCardinalNum().toString()));
                    if (Objects.equals(userInfo.getCardinalNum(), LocalDateTime.now().getYear() - 9)) { // 올해 연수생들은 게시판 추가
                        boardIdList.add(10L);
                        boardIdList.add(11L);
                        boardIdList.add(99L);
                    }
                } else if (Objects.equals(userInfo.getUserType(), "멘토")) {
                    boardIdList.add(100L);
                } else if (Objects.equals(userInfo.getUserType(), "사무국") || Objects.equals(userInfo.getUserType(), "관리자")) {
                    boardIdList.add(10L);
                    boardIdList.add(11L);
                    boardIdList.add(99L);
                    boardIdList.add(100L);
                    boardIdList.add(101L);
                    boardIdList.add(102L);
                    boardIdList.add(103L);
                    boardIdList.add(104L);
                    boardIdList.add(105L);
                    boardIdList.add(106L);
                    boardIdList.add(107L);
                    boardIdList.add(108L);
                    boardIdList.add(109L);
                    boardIdList.add(110L);
                    boardIdList.add(111L);
                    boardIdList.add(112L);
                    boardIdList.add(113L);
                    boardIdList.add(114L);
                }
            }
        });

        List<Posts> recentPostEntityList = postsRepository.findResentPosts(boardIdList, blockUserIdList, PageRequest.of(0, 4));
        List<Posts> freePostEntityList = postsRepository.findByBoard(1L, blockUserIdList, PageRequest.of(0, 4));
        List<Posts> applicantPostEntityList = postsRepository.findByBoard(4L, blockUserIdList, PageRequest.of(0, 4));

        return new MainPagePostsResponseDto(
                recentPostEntityList.stream()
                        .map(PostsResponseDto::new)
                            .collect(Collectors.toList()),
                freePostEntityList.stream()
                        .map(PostsResponseDto::new)
                        .collect(Collectors.toList()),
                applicantPostEntityList.stream()
                        .map(PostsResponseDto::new)
                        .collect(Collectors.toList())
        );
    }
}
