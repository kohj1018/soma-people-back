package kr.somapeople.somapeopleback.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query(value = "SELECT u FROM Users u WHERE u.userId = ?1 AND u.isDelete = false")  // 탈퇴한 유저는 조회되지 않도록 설정
    Optional<Users> findById(Long userId);

    @Query(value = "SELECT u FROM Users u WHERE u.oauthId = ?1 AND u.isDelete = false")
    Optional<Users> findByOauthId(String oauthId);
}
