package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandStatusEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Long> {
//
//  List<CommandEntity> findAllByStore(StoreEntity store);
//
//  Optional<CommandEntity> findCommandBySlotTimeAndStore(LocalDateTime SlotTime, StoreEntity store);
//
//  @Query(value = "select count(*) from sc_lalamiam.command", nativeQuery = true)
//  public long countAll();

  @Query(value = "select * from sc_lalamiam.command where slot_time between ?1 and ?2 and store_id=?3", nativeQuery = true)
  public List<CommandEntity> findCommandsByStoreIdDate(LocalDateTime startOfDay, LocalDateTime endOfDay, Long storeId);

  public Optional<CommandEntity> findFirstCommandByStoreIn(List<StoreEntity> stores);

  public List<CommandEntity> findCommandByStoreAndSlotTimeBetweenOrderBySlotTimeAsc(StoreEntity store, LocalDateTime startOfDay, LocalDateTime endOfDay);

  public List<CommandEntity> findCommandByStoreAndSlotTimeBetweenAndCommandStatusOrderBySlotTimeAsc(
          StoreEntity store,
          LocalDateTime startOfDay,
          LocalDateTime endOfDay,
          CommandStatusEntity commandStatus);

  public List<CommandEntity> findCommandByStoreAndSlotTimeBetweenAndCommandStatusInOrderBySlotTimeAsc(
          StoreEntity store,
          LocalDateTime startOfDay,
          LocalDateTime endOfDay,
          List<CommandStatusEntity> commandStatusList);

  public List<CommandEntity> findCommandByStoreAndSlotTimeBetweenAndCommandCode(
          StoreEntity store,
          LocalDateTime startOfDay,
          LocalDateTime endOfDay,
          String commandCode
  );
}
