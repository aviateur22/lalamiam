package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.CookEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface CookRepository extends JpaRepository<CookEntity, BigInteger> {
  @Query(value = "select count(*) from sc_lalamiam.cook", nativeQuery = true)
  public long countAll();
  @Query( value = "" +
                  "select * " +
                  "from sc_lalamiam.cook " +
                  "where store_id = ?1 and command_id = ?2 and product_id = ?3 " +
                  "order by created_at desc",
          nativeQuery = true)
  public Optional <CookEntity> findOneByStoreIdCommandIdProductId(
          BigInteger storeId,
          BigInteger commandId,
          BigInteger produtcId);

  @Query(value = "select * " +
          "from sc_lalamiam.cook " +
          "where store_id = ?1 and command_id = ?2 "+
          "order by created_at desc",
        nativeQuery = true)
  public List<CookEntity> findByCommandIdAndStoreId(BigInteger storeId, BigInteger commandId);

  @Modifying
  @Transactional
  @Query(value = "delete from sc_lalamiam.cook where command_id = ?1 and product_id = ?2 and store_id = ?3", nativeQuery = true)
  public int deleteProductInCommandByCommandIdStoreIdProductId(BigInteger commandId, BigInteger productId, BigInteger storeId);
}
