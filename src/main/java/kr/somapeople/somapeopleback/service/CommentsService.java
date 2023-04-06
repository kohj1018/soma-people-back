package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.blockUserLogs.BlockUserLogsRepository;
import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.comments.CommentsRepository;
import kr.somapeople.somapeopleback.domain.notificationLogs.NotificationLogsRepository;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.posts.PostsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsResponseDto;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsSaveRequestDto;
import kr.somapeople.somapeopleback.web.comments.dto.CommentsUpdateRequestDto;
import kr.somapeople.somapeopleback.web.comments.dto.RepliesResponseDto;
import kr.somapeople.somapeopleback.web.fcmNotification.dto.FCMNotificationRequestDto;
import kr.somapeople.somapeopleback.web.notificationLogs.dto.NotificationLogsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final BlockUserLogsRepository blockUserLogsRepository;
    private final FCMNotificationService fcmNotificationService;
    private final NotificationLogsRepository notificationLogsRepository;

    @Transactional
    public Long save(CommentsSaveRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + requestDto.getPostId()));

        Users user = usersRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + requestDto.getUserId()));

        Long commentId = commentsRepository.save(requestDto.toEntity(post, user)).getCommentId();   // 댓글 저장

        // 댓글 작성자가 글쓴이가 아닌 경우 해당 글쓴이에게 notification을 보냄
        if (requestDto.getRefId() == 0) {   // 게시글에 단 댓글인 경우
            if (!Objects.equals(post.getUser().getUserId(), user.getUserId())) {
                Optional<Users> targetUser = usersRepository.findById(post.getUser().getUserId());

                targetUser.ifPresent(
                    targetUserInfo -> {
                        fcmNotificationService.sendNotificationByToken(
                            FCMNotificationRequestDto.builder()
                                .targetUserId(targetUserInfo.getUserId())
                                .title("댓글 알림")
                                .body(requestDto.getContent())
                                .build()
                        );

                        notificationLogsRepository.save(NotificationLogsSaveRequestDto.builder()
                            .sendingUserId(user.getUserId())
                            .targetUserId(targetUserInfo.getUserId())
                            .postId(post.getPostId())
                            .boardName(post.getBoard().getName())
                            .notificationType("댓글")
                            .content(requestDto.getContent())
                            .build()
                            .toEntity());
                    }
                );
            }
        } else {    // 대댓글인 경우
            Comments refComment = commentsRepository.findById(requestDto.getRefId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. commentId=" + requestDto.getRefId()));

            List<Long> targetUserIdList = new ArrayList<>();    // 대댓글 알림을 보낼 대상들
            if (!Objects.equals(refComment.getUser().getUserId(), user.getUserId())) {
                targetUserIdList.add(refComment.getUser().getUserId());
            }
            commentsRepository.findByRefId(refComment.getCommentId())  // 대댓글 단 댓글에 달려있던 대댓글들을 모두 조회
                .forEach(commentInfo -> {
                    if (!Objects.equals(commentInfo.getUser().getUserId(), user.getUserId())    // 대댓글 작성자가 본인과 다르고
                        || !targetUserIdList.contains(commentInfo.getUser().getUserId())) {     // 리스트에 이미 넣은 값이 아니라면 추가 (중복방지)
                        targetUserIdList.add(commentInfo.getUser().getUserId());
                    }
                });

            targetUserIdList.forEach(targetUserId -> {  // 리스트에 있는 모두에게 알림 전송
                Optional<Users> targetUser = usersRepository.findById(targetUserId);

                if (targetUser.isPresent()) {
                    fcmNotificationService.sendNotificationByToken(
                        FCMNotificationRequestDto.builder()
                            .targetUserId(targetUserId)
                            .title("대댓글 알림")
                            .body(requestDto.getContent())
                            .build()
                    );

                    notificationLogsRepository.save(NotificationLogsSaveRequestDto.builder()
                        .sendingUserId(user.getUserId())
                        .targetUserId(targetUserId)
                        .postId(post.getPostId())
                        .boardName(post.getBoard().getName())
                        .notificationType("대댓글")
                        .content(requestDto.getContent())
                        .build()
                        .toEntity());
                }
            });
        }


        return commentId;
    }

    @Transactional
    public Long update(Long commentId, CommentsUpdateRequestDto requestDto) {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + commentId));

        // 대댓글이 달려있으면 삭제할 수 없도록 처리
        if (requestDto.getIsDelete()) {
            List<Comments> replyList = commentsRepository.findByRefId(commentId);

            if (!replyList.isEmpty()) {
                throw new IllegalArgumentException("해당 댓글에 대댓글이 달려있어 삭제할 수 없습니다. id=" + commentId);
            }
        }

        comment.update(requestDto.getContent(), requestDto.getIsAnonymous(), requestDto.getIsDelete());

        return commentId;
    }

    public List<CommentsResponseDto> findAllCommentsOnPost(Long postId, Long userId) {
        List<Long> blockUserIdList = blockUserLogsRepository.getAllBlockUserIdByUser(userId);

        if (blockUserIdList.isEmpty()) {    // TODO : SQL의 NOT IN에 빈 리스트가 들어가면 값 반환이 안돼 0을 추가해줌. (임시 해결책이라 추후 NOT IN 대신 LEFT JOIN을 활용한 방식으로 해결할 수 있을 듯)
            blockUserIdList.add(0L);
        }

        List<Comments> commentsList = commentsRepository.findAllCommentsOnPost(postId, blockUserIdList);

        return commentsList.stream()
                .filter(comment -> comment.getRefId() == 0)
                .map(comment -> {
                    List<RepliesResponseDto> repliesResponseDtoList = commentsList.stream()
                        .filter(_comment -> Objects.equals(_comment.getRefId(), comment.getCommentId()))
                        .map(RepliesResponseDto::new)
                        .collect(Collectors.toList());
                    return new CommentsResponseDto(comment, repliesResponseDtoList);
                })
                .collect(Collectors.toList());
    }

    public List<CommentsResponseDto> findByUserId(Long userId) {
        List<Comments> commentsList = commentsRepository.findByUserId(userId);

        return commentsList.stream()
                .map(comment -> new CommentsResponseDto(comment, new ArrayList<>()))
                .collect(Collectors.toList());
    }
}
