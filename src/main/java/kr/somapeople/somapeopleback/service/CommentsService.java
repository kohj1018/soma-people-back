package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.comments.CommentsRepository;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.posts.PostsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsResponseDto;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsSaveRequestDto;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public Long save(CommentsSaveRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + requestDto.getPostId()));

        Users user = usersRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + requestDto.getUserId()));

        return commentsRepository.save(requestDto.toEntity(post, user)).getCommentId();
    }

    @Transactional
    public Long update(Long commentId, CommentsUpdateRequestDto requestDto) {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + commentId));

        comment.update(requestDto.getContent(), requestDto.getIsAnonymous(), requestDto.getIsDelete());

        return commentId;
    }

    public List<CommentsResponseDto> findAllCommentsOnPost(Long postId) {
        List<Comments> entityList = commentsRepository.findAllCommentsOnPost(postId);

        return entityList.stream()
                .map(CommentsResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<CommentsResponseDto> findByUserId(Long userId) {
        List<Comments> entityList = commentsRepository.findByUserId(userId);

        return entityList.stream()
                .map(CommentsResponseDto::new)
                .collect(Collectors.toList());
    }
}
