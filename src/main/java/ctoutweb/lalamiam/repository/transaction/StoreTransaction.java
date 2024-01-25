package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.repository.*;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.ScheduleEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Set;

@Component
public class StoreTransaction {
  private final CommandRepository commandRepository;
  private final ProductRepository productRepository;
  private final ScheduleRepository scheduleRepository;
  private final StoreRepository storeRepository;
  private final ProRepository proRepository;

  public StoreTransaction(CommandRepository commandRepository, ProductRepository productRepository, ScheduleRepository scheduleRepository, StoreRepository storeRepository, ProRepository proRepository) {
    this.commandRepository = commandRepository;
    this.productRepository = productRepository;
    this.scheduleRepository = scheduleRepository;
    this.storeRepository = storeRepository;
    this.proRepository = proRepository;
  }

  /**
   * Sauvegarde des horaires du magasin
   * @param addStore
   */
  @Transactional()
  public BigInteger SaveStore(AddStoreDto addStore) {
    // Vérification existence pro
    proRepository.findById(addStore.proId()).orElseThrow(()->new RuntimeException("Le professionel n'existe pas"));

    // Sauvegarde du Store
    StoreEntity createdStore = storeRepository.save(new StoreEntity(addStore));

    // Sauvegarde des horaires
    addStore.storeSchedule().stream().forEach(storeSchedule->scheduleRepository.save(Factory.getSchedule(storeSchedule, createdStore.getId())));

    return createdStore.getId();
  }

  @Transactional
  public StoreEntity getCompleteStoreInformation(BigInteger storeId) {
    StoreEntity store = storeRepository.findById(storeId).get();
    Set<ScheduleEntity> schedules = scheduleRepository.findAllByStore(Factory.getStore(storeId));
    Set<CommandEntity> commands = commandRepository.findAllByStore(Factory.getStore(storeId));
    Set<ProductEntity> products = productRepository.findAllByStore(Factory.getStore(storeId));
    store.setSchedules(schedules);
    store.setCommands(commands);
    store.setProducts(products);
    return store;
  }
}
