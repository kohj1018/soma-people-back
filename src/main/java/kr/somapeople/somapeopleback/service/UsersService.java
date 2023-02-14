package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.users.dto.UsersResponseDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersSaveRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public Long save(UsersSaveRequestDto requestDto) {
        return usersRepository.save(requestDto.toEntity()).getUserId();
    }

    @Transactional
    public Long update(String oauthId, UsersUpdateRequestDto requestDto) {
        Users user = usersRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. oauthId=" + oauthId));

        user.update(requestDto.getName(), requestDto.getIsDelete());

        return user.getUserId();
    }

    public UsersResponseDto findById(Long userId) {
        Users entity = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + userId));

        return new UsersResponseDto(entity);
    }
}
