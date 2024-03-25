package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.JwtUserEntity;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtUserRepository extends JpaRepository<JwtUserEntity, Long> {

  Optional<JwtUserEntity> findOneByUserAndJwt(UserEntity user, String jwt);

  Optional<JwtUserEntity> findOneByUser(UserEntity user);
}
