package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, BigInteger> {
  @Query(value = "select count(*) from sc_lalamiam.product", nativeQuery = true)
  public long countAll();
}
