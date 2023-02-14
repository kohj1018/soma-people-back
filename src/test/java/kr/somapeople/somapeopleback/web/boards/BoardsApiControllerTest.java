package kr.somapeople.somapeopleback.web.boards;

import kr.somapeople.somapeopleback.domain.boards.Boards;
import kr.somapeople.somapeopleback.domain.boards.BoardsRepository;
import kr.somapeople.somapeopleback.web.boards.dto.BoardsResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BoardsRepository boardsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        boardsRepository.deleteAll();
    }

    @DisplayName("전체 게시판 목록 불러오기")
    @Test
    public void 전체_게시판_목록_불러오기() throws Exception {
        //given
        String name1 = "자유";
        String name2 = "질문답변";
        String name3 = "취업/이직";

        boardsRepository.save(new Boards(name1));
        boardsRepository.save(new Boards(name2));
        boardsRepository.save(new Boards(name3));

        String url = "http://localhost:" + port + "/api/v1/boards";

        //when
        ResponseEntity<BoardsResponseDto[]> responseEntity = restTemplate.getForEntity(url, BoardsResponseDto[].class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        List<BoardsResponseDto> boardsList = Arrays.asList(responseEntity.getBody());
        assertThat(boardsList.get(0).getName()).isEqualTo(name1);
        assertThat(boardsList.get(1).getName()).isEqualTo(name2);
        assertThat(boardsList.get(2).getName()).isEqualTo(name3);
    }
}