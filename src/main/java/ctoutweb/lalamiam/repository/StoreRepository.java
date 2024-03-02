package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
  @Query(value = "select count(*) from sc_lalamiam.store", nativeQuery = true)
  public long countAll();
}
