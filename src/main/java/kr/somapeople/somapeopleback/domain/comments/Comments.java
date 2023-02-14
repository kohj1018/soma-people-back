package kr.somapeople.somapeopleback.domain.comments;

import kr.somapeople.somapeopleback.domain.BaseTimeEntity;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "comments")
@Entity
public class Comments extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @Builder
    public Comments(Posts post, Users user, String content, Boolean isAnonymous) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDelete = false;
    }

    public void update(String content, Boolean isAnonymous, Boolean isDelete) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isDelete = isDelete;
    }
}
