package kr.somapeople.somapeopleback.domain.boards;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardsRepositoryTest {

    @Autowired
    BoardsRepository boardsRepository;

    @AfterEach
    public void cleanup() {
        boardsRepository.deleteAll();
    }

    @DisplayName("게시판저장_불러오기")
    @Test
    public void 게시판저장_불러오기() {
        //given
        String name = "자유";
        boardsRepository.save(new Boards(name));

        //when
        List<Boards> boardsList = boardsRepository.findAll();

        //then
        Boards board = boardsList.get(0);
        assertThat(board.getName()).isEqualTo(name);
    }
}