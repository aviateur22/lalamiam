package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, BigInteger> {
  @Query(value = "select count(*) from sc_lalamiam.command", nativeQuery = true)
  public long countAll();


}
