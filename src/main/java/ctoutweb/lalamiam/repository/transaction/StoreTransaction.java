package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.DailyStoreSchedule;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.repository.*;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.WeekDayEntity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreTransaction {

  private final EntityManagerFactory entityManagerFactory;
  private final StoreDayScheduleRepository storeDayScheduleRepository;
  private final StoreRepository storeRepository;
  private final ProRepository proRepository;

  public StoreTransaction(
          CommandRepository commandRepository,
          ProductRepository productRepository,
          EntityManagerFactory entityManagerFactory,
          StoreDayScheduleRepository scheduleRepository,
          StoreRepository storeRepository,
          ProRepository proRepository
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
  public BigInteger SaveStore(AddStoreDto addStore) {
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
  public StoreEntity getCompleteStoreInformation(BigInteger storeId) {
    StoreEntity store = null;
    Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
    Transaction transaction = session.beginTransaction();
    try {
      store = session.get(StoreEntity.class, storeId);
      Hibernate.initialize(store.getCommands());
      Hibernate.initialize(store.getProducts());
      Hibernate.initialize(store.getStoreWeekDaySchedules());
      transaction.commit();
    } catch (Exception ex) {

    } finally {
      session.close();
      return store;
    }
  }

}
