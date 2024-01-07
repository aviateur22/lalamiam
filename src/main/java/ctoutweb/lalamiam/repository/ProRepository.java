package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ProEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ProRepository extends JpaRepository<ProEntity, BigInteger> {
}
