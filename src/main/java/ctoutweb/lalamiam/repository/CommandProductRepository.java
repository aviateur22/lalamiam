package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandProductRepository extends JpaRepository<CommandProductEntity, Long> {
//  @Query(value = "select count(*) from sc_lalamiam.command_product", nativeQuery = true)
//  public long countAll();
//  @Query( value = "" +
//          "select * " +
//          "from sc_lalamiam.command_product " +
//          "where command_id = ?1 and product_id = ?2 " +
//          "order by created_at desc " +
//          "limit 1",
//          nativeQuery = true)
//  public Optional <CommandProductEntity> findOneProductByCommandIdProductId(
//          BigInteger commandId,
//          BigInteger produtcId);
//
//  @Query(value = "select * " +
//          "from sc_lalamiam.command_product " +
//          "where command_id = ?1 "+
//          "order by created_at desc",
//        nativeQuery = true)
//  public List<CommandProductEntity> findProductsByCommandId(Long commandId);
//
//  @Modifying
//  @Transactional
//  @Query(value = "delete from sc_lalamiam.command_product where command_id = ?1 and product_id = ?2", nativeQuery = true)
//  public int deleteProductByCommandIdAndProductId(Long commandId, Long productId);

  @Modifying
  @Transactional
  @Query(value = "delete from sc_lalamiam.command_product where command_id = ?1", nativeQuery = true)
  public int deleteProductByCommandId(Long commandId);
}
