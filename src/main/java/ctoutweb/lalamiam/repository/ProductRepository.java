package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  /**
   * Recherche des produits d'un commerce
   * @param store StoreEntity - Store
   * @return List<ProductEntity>
   */
  public List<ProductEntity> findByStore(StoreEntity store);

}
