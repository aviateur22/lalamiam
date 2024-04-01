package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.model.dto.CreateStoreDto;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.StoreDayScheduleRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.WeekDayEntity;
import ctoutweb.lalamiam.repository.transaction.StoreTransaction;
import ctoutweb.lalamiam.service.StoreService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StoreServiceImp implements StoreService {
 private final StoreTransaction storeTransaction;
 private final StoreDayScheduleRepository storeDayScheduleRepository;
 private final StoreRepository storeRepository;
 private final ProductRepository productRepository;
  public StoreServiceImp(
          StoreTransaction storeTransaction,
          StoreDayScheduleRepository storeDayScheduleRepository,
          StoreRepository storeRepository,
          ProductRepository productRepository
  ) {
    this.storeTransaction = storeTransaction;
    this.storeDayScheduleRepository = storeDayScheduleRepository;
    this.storeRepository = storeRepository;
    this.productRepository = productRepository;
  }

  @Override
  public CreateStoreDto createStore(AddStoreDto addStore) {
    String name = addStore.name();
    String adress = addStore.adress();
    String city = addStore.city();
    String cp = addStore.cp();
    Long proId = addStore.proId();

    if(CommonFunction.isNullOrEmpty(name)||
            CommonFunction.isNullOrEmpty(adress) ||
            CommonFunction.isNullOrEmpty(city) ||
            CommonFunction.isNullOrEmpty(cp) ||
            proId == null) throw new RuntimeException("données manquantes");

    Long storeId = storeTransaction.SaveStore(addStore);
    CreateStoreDto store = storeTransaction.getCompleteStoreInformation(storeId);
    return store;
  }

  @Override
  public StoreEntity findStoreById(Long storeId) {
    return storeRepository.findById(storeId).orElseThrow(()->new RuntimeException("Le Commerce n'existe pas"));
  }

  @Override
  public List<StoreDayScheduleEntity> findStoreSchedulesByDay(StoreEntity store, WeekDayEntity weekDay){
    return storeDayScheduleRepository.findAllByStoreAndWeekDay(store, weekDay);
  }

  @Override
  public List<LocalDateTime> findStorSlotsWithoutConstraintByDay(LocalDateTime startOfCommandDay, Integer storeFrequenceSlot) {
    //todo faire test unitaire

    // Nombre de Slots disponible sur 24h pour le commerce
    Integer slotAvailibilityInOneDay = calculateNumberOfCommandSlotForOneDay(storeFrequenceSlot);

    // Génération d'une liste de créneaux sur 24h ne prenant pas en compte les contraintes
    return Stream.iterate(
            startOfCommandDay, dateTime-> dateTime.plusMinutes(storeFrequenceSlot)
    )
    .limit(slotAvailibilityInOneDay)
    .toList();
  }

  /**
   * Calcul du nombre de créneaux pour commande disponable sur 24h pour 1 magasin
   * Ne tient pas compte des commandes déja présentes
   * @param storeFrequenceSlot Integer
   * @return Integer Nombre de slotDisponible sur 24h
   */
  private Integer calculateNumberOfCommandSlotForOneDay(Integer storeFrequenceSlot) {
    //todo faire test unitaire

    // Nombre de minute dans 1h
    final int MINUTES_IN_HOUR = 60;

    // Nombre d' heure dans 1 journée
    final int HOUR_IN_DAY = 24;

    // Nombre d'itération de commande par heure
    final int ITERATION_PER_HOUR = MINUTES_IN_HOUR / storeFrequenceSlot;

    // Nombre d'iteration pour 24h
    return ITERATION_PER_HOUR * HOUR_IN_DAY;
  }
}
