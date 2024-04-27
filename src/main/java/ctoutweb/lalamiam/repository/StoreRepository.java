package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

  /**
   * Recuperation list<store> par proId
   * @param pro UserEntity
   * @return List<StoreEntity>
   */
  List<StoreEntity> findAllByPro(UserEntity pro);
}
