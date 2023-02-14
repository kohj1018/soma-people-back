package kr.somapeople.somapeopleback.domain.boards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public Boards(String name) {
        this.name = name;
    }
}
