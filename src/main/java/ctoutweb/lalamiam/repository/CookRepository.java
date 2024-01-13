package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.CookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface CookRepository extends JpaRepository<CookEntity, BigInteger> {
  @Query(value = "select count(*) from sc_lalamiam.cook", nativeQuery = true)
  public long countAll();
  @Query(
          value = "" +
                  "select * " +
                  "from sc_lalamiam.cook " +
                  "where store_id = ?1 and command_id = ?2 and product_id = ?3 " +
                  "order by created_at desc",
          nativeQuery = true)
  public Optional <CookEntity> findOneByStoreIdCommandIdProductId(
          BigInteger storeId,
          BigInteger commandId,
          BigInteger produtcId);


}
