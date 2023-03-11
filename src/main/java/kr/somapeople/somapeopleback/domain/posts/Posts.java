package kr.somapeople.somapeopleback.domain.posts;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EntityListeners(AuditingEntityListener.class)  // 이 클래스에 Auditing 기능을 포함시킨다.
@NoArgsConstructor
@Table(name = "posts")
@Entity
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id", nullable = false)
    private Boards board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    @Column(nullable = false)
    private Long hits;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")   // Comments와의 양방향 매핑을 위해 추가
    private List<Comments> commentsList = new ArrayList<>();

    @Builder
    public Posts(Boards board, Users user, String title, String content, Boolean isAnonymous) {
        this.board = board;
        this.user = user;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.hits = 0L;
        this.isDelete = false;
        this.updatedAt = null;
    }

    public void update(Boards board, String title, String content, Boolean isAnonymous, Boolean isDelete) {
        this.board = board;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDelete = isDelete;
        this.updatedAt = LocalDateTime.now();
    }

    public void addHits(Long postAuthorId, Long userId) {
        if (!postAuthorId.equals(userId)) { // 글 작성자가 조회하는 경우는 조회수 증가 안함
            this.hits += 1;
        }
    }
}
