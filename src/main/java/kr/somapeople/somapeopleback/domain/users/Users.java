package kr.somapeople.somapeopleback.domain.users;

import kr.somapeople.somapeopleback.domain.BaseTimeEntity;
import kr.somapeople.somapeopleback.domain.blockUserLogs.BlockUserLogs;
import kr.somapeople.somapeopleback.domain.comments.Comments;
import kr.somapeople.somapeopleback.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(name = "user_type", length = 20, nullable = false)
    private String userType;

    @Column(name = "cardinal_num", nullable = true)
    private Integer cardinalNum;

    @Column(name = "is_certified", nullable = false)
    private Boolean isCertified;

    @Column(name = "oauth_id", length = 100, nullable = false)
    private String oauthId;

    @Column(name = "refresh_token", length = 100, nullable = false)
    private String refreshToken;

    @Column(name = "agree_terms", nullable = false)
    private Boolean agreeTerms;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")   // Posts 와의 양방향 매핑을 위해 추가
    private List<Posts> postsList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")   // Comments 와의 양방향 매핑을 위해 추가
    private List<Comments> commentsList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")   // BlockUserLogs 와의 양방향 매핑을 위해 추가
    private List<BlockUserLogs> blockUserLogsList = new ArrayList<>();

    @Builder
    public Users(String name, String userType, int cardinalNum, Boolean isCertified, String oauthId, String refreshToken, Boolean agreeTerms) {
        this.name = name;
        this.userType = userType;
        this.cardinalNum = cardinalNum;
        this.isCertified = isCertified;
        this.oauthId = oauthId;
        this.refreshToken = refreshToken;
        this.agreeTerms = agreeTerms;
        this.isDelete = false;
    }

    public void update(String name, Boolean isDelete) {
        this.name = name;
        this.isDelete = isDelete;
    }
}
