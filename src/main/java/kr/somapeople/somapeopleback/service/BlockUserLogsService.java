package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.blockUserLogs.BlockUserLogs;
import kr.somapeople.somapeopleback.domain.blockUserLogs.BlockUserLogsRepository;
import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.blockUserLogs.dto.BlockUserLogsSaveRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BlockUserLogsService {

    private final BlockUserLogsRepository blockUserLogsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public Long save(BlockUserLogsSaveRequestDto requestDto) {
        Optional<BlockUserLogs> log = blockUserLogsRepository.findByUserIdAndBlockUserId(requestDto.getUserId(), requestDto.getBlockUserId());

        if (log.isPresent()) {
            throw new IllegalArgumentException("이미 차단했습니다.");
        }

        Users user = usersRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + requestDto.getUserId()));

        if (Objects.equals(user.getUserId(), requestDto.getUserId())) {
            throw new IllegalArgumentException("자신은 차단할 수 없습니다.");
        }

        return blockUserLogsRepository.save(requestDto.toEntity(user)).getBlockUserLogId();
    }

    @Transactional
    public void delete(Long userId, Long blockUserId) {
        Optional<BlockUserLogs> log = blockUserLogsRepository.findByUserIdAndBlockUserId(userId, blockUserId);

        if (log.isPresent()) {
            blockUserLogsRepository.deleteById(log.get().getBlockUserLogId());
        } else {
            throw new IllegalArgumentException("차단 기록이 존재하지 않습니다.");
        }
    }

    public List<UsersResponseDto> getAllBlockUser(Long userId) {
        List<Long> blockUserIdList = blockUserLogsRepository.getAllBlockUserIdByUser(userId);

        List<UsersResponseDto> responseDtoList = new ArrayList<>();

        if (blockUserIdList != null && !blockUserIdList.isEmpty()) {
            blockUserIdList.forEach(blockUserId -> {
                usersRepository.findById(blockUserId)
                        .ifPresent(user -> responseDtoList.add(new UsersResponseDto(user)));
            });
        }

        return responseDtoList;
    }
}
