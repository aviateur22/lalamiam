package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ClientCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClientCommandRepository extends JpaRepository<ClientCommandEntity, Long> {
}
