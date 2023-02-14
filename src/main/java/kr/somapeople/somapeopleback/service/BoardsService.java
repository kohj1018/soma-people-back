package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.boards.BoardsRepository;
import kr.somapeople.somapeopleback.web.boards.dto.BoardsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardsService {

    private final BoardsRepository boardsRepository;

    public List<BoardsResponseDto> findAll() {
       return boardsRepository.findAll().stream()
               .map(BoardsResponseDto::new).collect(Collectors.toList());
    }
}
