package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.model.dto.CreateStoreDto;
import ctoutweb.lalamiam.repository.*;
import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.WeekDayEntity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StoreTransaction {

  private final EntityManagerFactory entityManagerFactory;
  private final StoreDayScheduleRepository storeDayScheduleRepository;
  private final StoreRepository storeRepository;
  private final UserRepository proRepository;

  public StoreTransaction(
          CommandRepository commandRepository,
          ProductRepository productRepository,
          EntityManagerFactory entityManagerFactory,
          StoreDayScheduleRepository scheduleRepository,
          StoreRepository storeRepository,
          UserRepository proRepository
  ) {
    this.entityManagerFactory = entityManagerFactory;
    this.storeDayScheduleRepository = scheduleRepository;
    this.storeRepository = storeRepository;
    this.proRepository = proRepository;
  }

  /**
   * Sauvegarde des horaires du magasin
   * @param addStore - AddStoreDto
   */
  @Transactional()
  public Long SaveStore(AddStoreDto addStore) {
    // Vérification existence pro
    proRepository.findById(addStore.proId()).orElseThrow(()->new RuntimeException("Le professionel n'existe pas"));

    // Sauvegarde du Store
    StoreEntity createdStore = storeRepository.save(new StoreEntity(addStore));

    // Sauvegarde des horaires
    addStore.weeklyStoreSchedules()
            .stream()
            .forEach(storeSchedule-> storeSchedule.days()
                .stream()
                .forEach(day->{
                  WeekDayEntity weekDay = Factory.getWeekDay(day);
                  storeDayScheduleRepository
                          .save(Factory.getStoreWeekDaySchedule(storeSchedule.storeSchedule(), weekDay, createdStore));
                })
            );

    return createdStore.getId();
  }


  /**
   * Renvoie les information complete d'un commerce
   * Commands - Products - Schedules ....
   * @param storeId - BigInteger
   * @return StoreEntity
   */
  public CreateStoreDto getCompleteStoreInformation(Long storeId) {

    // Recherche store
    StoreEntity store = storeRepository.findById(storeId)
      .orElse(null);

    if(store == null) return null;

    // Schedules
    List<StoreDayScheduleEntity> schedules = storeDayScheduleRepository.findAllByStore(store);
    store.setStoreWeekDaySchedules(schedules);

    return Factory.createStoreDto(store);
  }

}
