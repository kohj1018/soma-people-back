package kr.somapeople.somapeopleback.domain.boards;

import kr.somapeople.somapeopleback.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "boards")
@Entity
public class Boards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board") // boards와의 양방향 매핑을 위해 추가
    private List<Posts> postsList = new ArrayList<>();


    public Boards(String name) {
        this.name = name;
    }
}
