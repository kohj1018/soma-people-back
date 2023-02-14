package kr.somapeople.somapeopleback.domain.blockUserLogs;

import kr.somapeople.somapeopleback.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "block_user_logs")
@Entity
public class BlockUserLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_user_log_id")
    private Long blockUserLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "block_user_id")
    private Long blockUserId;

    @Builder
    public BlockUserLogs(Users user, Long blockUserId) {
        this.user = user;
        this.blockUserId = blockUserId;
    }
}
