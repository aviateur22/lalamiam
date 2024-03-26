package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.repository.entity.RoleUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleUserRepository extends JpaRepository<RoleUserEntity, Long> {
}
