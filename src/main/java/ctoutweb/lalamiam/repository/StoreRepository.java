package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, BigInteger> {
}
