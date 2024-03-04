package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.WeekDayEntity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface StoreService {
  /**
   * Création d'un commerce
   * @param addStoreSchema
   * @return
   */
  public StoreEntity createStore(AddStoreDto addStoreSchema);

  /**
   * Recherche d'un commerce
   * @param storeId  BigInteger - Identifiant commerce
   * @return StoreEntity
   */
  public StoreEntity findStoreById(Long storeId);

  /**
   * Recherche la liste des creneaux d'ouverture d'un commerce sur une journée
   * @param store  StoreEntity - Commerce
   * @param weekDay WeekDayEntity - Jour de recherche
   * @return List<StoreDayScheduleEntity>
   */
  public List<StoreDayScheduleEntity> findStoreSchedulesByDay(StoreEntity store, WeekDayEntity weekDay);

  /**
   *
   * @param startOfCommandDay
   * @param storeFrequenceSlotTime
   * @return
   * @throws Exception
   */
  public List<LocalDateTime> findStorSlotsWithoutConstraintByDay(LocalDateTime startOfCommandDay, Integer storeFrequenceSlotTime);
}
