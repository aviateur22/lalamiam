package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, BigInteger> {
  Set<ProductEntity> findAllByStore(StoreEntity store);
  @Query(value = "select count(*) from sc_lalamiam.product", nativeQuery = true)
  public long countAll();

  /**
   * Cherche un produit par ProductId and StoreId
   * @param storeId
   * @param productId
   * @return
   */
  public Optional<ProductEntity> findByStoreAndId(BigInteger storeId, BigInteger productId);

  /**
   * Cherche tous les produits d'un store
   * @param store
   * @return
   */
  public List<ProductEntity> findByStore(StoreEntity store);
}
