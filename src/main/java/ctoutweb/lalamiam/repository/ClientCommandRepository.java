package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ClientCommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientCommandRepository extends JpaRepository<ClientCommandEntity, Long> {
  Optional<ClientCommandEntity> findOneByUserAndCommand(UserEntity user, CommandEntity command);
}
