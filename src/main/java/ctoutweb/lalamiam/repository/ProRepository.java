package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ProEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface ProRepository extends JpaRepository<ProEntity, BigInteger> {

  @Query(value = "select count(*) from sc_lalamiam.pro", nativeQuery = true)
  Integer countProInDatabase();

  @Modifying
  @Transactional
  @Query(value = "truncate sc_lalamiam.pro cascade", nativeQuery = true)
  Integer truncateAll();
}
