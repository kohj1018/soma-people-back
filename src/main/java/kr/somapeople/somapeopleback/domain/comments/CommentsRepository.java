package kr.somapeople.somapeopleback.domain.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    @Query(value = "SELECT c FROM Comments c WHERE c.post.postId = ?1 AND c.isDelete = false ORDER BY c.commentId DESC")
    List<Comments> findAllCommentsOnPost(Long postId);

    @Query(value = "SELECT c FROM Comments c WHERE c.user.userId = ?1 AND c.isDelete = false ORDER BY c.commentId DESC")
    List<Comments> findByUserId(Long userId);
}
