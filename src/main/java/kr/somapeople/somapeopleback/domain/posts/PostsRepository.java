package kr.somapeople.somapeopleback.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query(value = "SELECT p FROM Posts p WHERE p.postId = ?1 AND p.isDelete = false")  // 삭제된 글은 조회되지 않도록 설정
    Optional<Posts> findById(Long postId);

    @Query(value = "SELECT p FROM Posts p WHERE p.board.boardId = ?1 AND p.postId < ?2 AND p.isDelete = false ORDER BY p.postId DESC")
    Page<Posts> findByPostIdLessThanOrderByPostIdDesc(Long boardId, Long lastPostId, PageRequest pageRequest);

    @Query(value = "SELECT p FROM Posts p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(?1), '%') OR LOWER(p.content) LIKE CONCAT('%', LOWER(?1), '%')) AND p.isDelete = false")
    List<Posts> searchAllPosts(String searchTerm);

    @Query(value = "SELECT p FROM Posts p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(?1), '%') OR LOWER(p.content) LIKE CONCAT('%', LOWER(?1), '%')) AND p.board.boardId = ?2 AND p.isDelete = false")
    List<Posts> searchPostsWithinGivenBoard(String searchTerm, Long boardIdToSearch);

    @Query(value = "SELECT p FROM Posts p WHERE p.user.userId = ?1 AND p.isDelete = false ORDER BY p.postId DESC")
    List<Posts> findByUserId(Long userId);
}
