package kr.somapeople.somapeopleback.service;

import kr.somapeople.somapeopleback.domain.users.Users;
import kr.somapeople.somapeopleback.domain.users.UsersRepository;
import kr.somapeople.somapeopleback.web.users.dto.UserCertificationUpdateRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersResponseDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersSaveRequestDto;
import kr.somapeople.somapeopleback.web.users.dto.UsersUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

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

    public Long findUserIdByOauthId(String oauthId) {
        Optional<Users> entity = usersRepository.findByOauthId(oauthId);

        if (entity.isPresent()) {
            return entity.get().getUserId();
        } else {
            return -1L;
        }
    }

    @Transactional
    public void registerFirebaseToken(Long userId, String firebaseToken) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + userId));

        user.registerFirebaseToken(firebaseToken);
    }

    @Transactional
    public void handleUserCertification(UserCertificationUpdateRequestDto requestDto) {
        Users adminUser = usersRepository.findByOauthId(requestDto.getAdminOauthId())
            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. oauthId=" + requestDto.getAdminOauthId()));

        if (!Objects.equals(adminUser.getUserType(), "관리자")) {
            throw new IllegalArgumentException("관리자만 소마인 인증을 진행할 수 있습니다.");
        }

        Users targetUser = usersRepository.findById(requestDto.getTargetUserId())
            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId=" + requestDto.getTargetUserId()));

        targetUser.handleUserCertification(requestDto.getNameToBeUpdated(), requestDto.getCardinalNumToBeUpdated());
    }
}
